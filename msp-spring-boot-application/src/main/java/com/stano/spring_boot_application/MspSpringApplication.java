package com.stano.spring_boot_application;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

/**
 * The platform's replacement for {@link SpringApplication#run(Class, String...)}.
 *
 * <p>Every application built on this platform should launch through {@link #run(Class, String...)}
 * rather than calling {@code SpringApplication.run} directly, so that platform-wide startup
 * behavior stays centralized in one place. Today that means redirecting anything written to {@code
 * System.out}/{@code System.err} (for example by third-party libraries that use {@code
 * System.out.println} instead of a logger) through SLF4J, so all application output ends up in the
 * structured log stream. Future cross-cutting startup concerns belong here too.
 *
 * <p>Example usage, in place of {@code SpringApplication.run(Application.class, args)}:
 *
 * <pre>{@code
 * @SpringBootApplication
 * public class Application {
 *   public static void main(String[] args) {
 *     MspSpringApplication.run(Application.class, args);
 *   }
 * }
 * }</pre>
 */
public class MspSpringApplication {
  /**
   * Redirects {@code System.out}/{@code System.err} output to SLF4J and then delegates to {@link
   * SpringApplication#run(Class, String...)} to bootstrap and run the application.
   *
   * @param primarySource the primary Spring component/configuration class, typically the class
   *     annotated with {@code @SpringBootApplication}
   * @param args the command-line arguments passed to {@code main}
   * @return the running application context
   */
  public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
    SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();

    return SpringApplication.run(primarySource, args);
  }
}
