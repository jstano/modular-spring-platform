package com.stano.spring_boot_application;

import com.stano.spring_boot_application.logging.DefaultLoggingContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

public class MspSpringApplication {
  public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
    DefaultLoggingContextInitializer.initialize();
    SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();

    return SpringApplication.run(primarySource, args);
  }
}
