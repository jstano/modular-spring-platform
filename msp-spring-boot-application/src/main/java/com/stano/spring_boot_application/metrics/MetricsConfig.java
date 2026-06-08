package com.stano.spring_boot_application.metrics;

import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.jolokia.server.core.config.ConfigKey;
import org.jolokia.server.core.http.AgentServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class MetricsConfig {
  private static final Logger log = LoggerFactory.getLogger(MetricsConfig.class);

  @Bean
  public PrometheusMeterRegistry prometheusMeterRegistry() {
    PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    new JvmMemoryMetrics().bindTo(registry);
    new JvmGcMetrics().bindTo(registry);
    new JvmThreadMetrics().bindTo(registry);
    new ProcessorMetrics().bindTo(registry);
    new UptimeMetrics().bindTo(registry);

    return registry;
  }

  @Bean
  public ServletRegistrationBean<AgentServlet> jolokia() {
    Map<String, String> params = Map.of(
      ConfigKey.AGENT_DESCRIPTION.getKeyValue(), "Spring Servlet Jolokia Agent"
    );

    ServletRegistrationBean<AgentServlet> jolokiaServlet = new ServletRegistrationBean<>(new AgentServlet(), "/jolokia/*");
    jolokiaServlet.setLoadOnStartup(0);
    jolokiaServlet.setAsyncSupported(true);
    jolokiaServlet.setInitParameters(params);
    return jolokiaServlet;
  }
}
