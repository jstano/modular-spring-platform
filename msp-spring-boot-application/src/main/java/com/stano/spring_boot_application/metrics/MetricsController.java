package com.stano.spring_boot_application.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/metrics")
public class MetricsController {
  private static final Logger log = LoggerFactory.getLogger(MetricsController.class);

  private final ObjectMapper objectMapper;
  private final ConfigurableEnvironment configurableEnvironment;
  private final MeterRegistry meterRegistry;
  private final PrometheusMeterRegistry registry;

  public MetricsController(
      ObjectMapper objectMapper,
      ConfigurableEnvironment configurableEnvironment,
      MeterRegistry meterRegistry,
      PrometheusMeterRegistry registry) {
    this.objectMapper = objectMapper;
    this.configurableEnvironment = configurableEnvironment;
    this.meterRegistry = meterRegistry;
    this.registry = registry;
  }

  @GetMapping
  public String getMetrics() {
    return registry.scrape();
  }

  @GetMapping("/environment")
  public ResponseEntity<?> getEnvironmentMetrics() throws JacksonException {
    // Build effective (resolved) properties — first source wins
    Map<String, Object> effectiveProperties = new LinkedHashMap<>();
    for (PropertySource<?> source : configurableEnvironment.getPropertySources()) {
      if (source instanceof EnumerablePropertySource<?> eps) {
        for (String key : eps.getPropertyNames()) {
          effectiveProperties.putIfAbsent(
              key, sanitize(key, configurableEnvironment.getProperty(key), false));
        }
      }
    }

    // Combine both into one response
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("activeProfiles", configurableEnvironment.getActiveProfiles());
    response.put("effectiveProperties", effectiveProperties);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(objectMapper.writeValueAsString(response));
  }

  @GetMapping("/jvm")
  public Map<String, Object> jvmMetrics() {
    Map<String, Object> result = new LinkedHashMap<>();
    meterRegistry.getMeters().stream()
        .filter(m -> m.getId().getName().startsWith("jvm."))
        .forEach(m -> result.put(m.getId().getName(), m.measure()));
    return result;
  }

  @GetMapping("/jvm-gc")
  public List<Map<String, Object>> gcInfo() {
    return ManagementFactory.getGarbageCollectorMXBeans().stream()
        .map(
            gc ->
                Map.<String, Object>of(
                    "name", gc.getName(),
                    "collectionCount", gc.getCollectionCount(),
                    "collectionTime", gc.getCollectionTime(),
                    "memoryPools", List.of(gc.getMemoryPoolNames())))
        .toList();
  }

  @GetMapping("/thread-dump")
  public ResponseEntity<?> threadDump() throws IOException {
    String timestamp = LocalDateTime.now().toString().replace("T", "-");
    long pid = ProcessHandle.current().pid();
    Process process = Runtime.getRuntime().exec(new String[] {"jstack", String.valueOf(pid)});
    String output;
    try (InputStream in = process.getInputStream()) {
      output = new String(in.readAllBytes());
    } finally {
      process.destroy();
    }

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            String.format("attachment; filename=thread-dump-%s.tdump", timestamp))
        .body(output);
  }

  @GetMapping("/heap-dump")
  public ResponseEntity<StreamingResponseBody> heapDump(
      @RequestParam(name = "live-only", defaultValue = "false") boolean liveOnly)
      throws IOException {
    File tempFile =
        new File(
            System.getProperty("java.io.tmpdir"),
            "heap-dump-" + System.currentTimeMillis() + ".hprof");
    tempFile.deleteOnExit();

    try {
      MBeanServer server = ManagementFactory.getPlatformMBeanServer();
      ObjectName hotspot = new ObjectName("com.sun.management:type=HotSpotDiagnostic");

      server.invoke(
          hotspot,
          "dumpHeap",
          new Object[] {tempFile.toPath().toAbsolutePath().toString(), liveOnly},
          new String[] {String.class.getName(), boolean.class.getName()});

      StreamingResponseBody stream =
          out -> {
            try (InputStream in = new FileInputStream(tempFile)) {
              in.transferTo(out);
            } finally {
              try {
                Files.deleteIfExists(tempFile.toPath());
              } catch (IOException deleteEx) {
                log.warn(
                    "Failed to delete temporary heap dump file {}", tempFile.toPath(), deleteEx);
              }
            }
          };

      LocalDateTime timestamp = LocalDateTime.now();

      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .header(
              HttpHeaders.CONTENT_DISPOSITION,
              String.format("attachment; filename=heap-dump-%s.hprof", timestamp))
          .body(stream);
    } catch (Exception e) {
      try {
        Files.deleteIfExists(tempFile.toPath());
      } catch (IOException deleteEx) {
        e.addSuppressed(deleteEx);
      }
      throw new IOException("Failed to generate heap dump", e);
    }
  }

  private Object sanitize(String key, Object value, boolean showSensitive) {
    if (showSensitive) {
      return value;
    }

    return Sanitizer.sanitize(key, value);
  }
}
