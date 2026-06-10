package com.stano.spring_boot_application;

import com.stano.spring_boot_application.logging.DefaultLoggingContextInitializer;
import com.stano.spring_boot_application.schema.SchemaMigrationRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

import java.util.Arrays;

public class MspSpringApplication {
  private static final Logger LOGGER = LoggerFactory.getLogger(MspSpringApplication.class);

  public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
    DefaultLoggingContextInitializer.initialize();
    SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();

    ConfigurableApplicationContext ctx = SpringApplication.run(primarySource, args);

    if (hasMigrateFlag(args)) {
      SchemaMigrationRequestedEvent event = new SchemaMigrationRequestedEvent(MspSpringApplication.class);
      ctx.publishEvent(event);
      if (event.isHandled()) {
        LOGGER.info("Schema migration completed successfully");
        System.exit(0);
      } else {
        LOGGER.warn("Schema migration requested (--migrate) but no migration handler is registered. " +
                    "Is msp-domain-jpa-starter configured with a SchemaContext bean?");
        System.exit(1);
      }
    }

    return ctx;
  }

  private static boolean hasMigrateFlag(String[] args) {
    return Arrays.stream(args).anyMatch(arg -> "--migrate".equals(arg));
  }
}
