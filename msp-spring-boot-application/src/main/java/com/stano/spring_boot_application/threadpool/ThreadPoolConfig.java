package com.stano.spring_boot_application.threadpool;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Auto-configuration providing the platform's default background {@link TaskExecutor} bean, used
 * for {@code @Async} method execution and other background task submission.
 */
@AutoConfiguration
public class ThreadPoolConfig {
  /**
   * Builds a thread-pooled {@link TaskExecutor} whose pool size and queue capacity are configurable
   * via the {@code app.task-executor.core-pool-size} (default 4), {@code
   * app.task-executor.max-pool-size} (default 10), and {@code app.task-executor.queue-capacity}
   * (default 1000) properties. Threads created by the pool are named with a {@code bg-task-}
   * prefix.
   *
   * @param environment the Spring environment, used to resolve pool-sizing properties
   * @return the configured, initialized task executor
   */
  @Bean
  public TaskExecutor taskExecutor(Environment environment) {
    int corePoolSize =
        environment.getProperty("app.task-executor.core-pool-size", Integer.class, 4);
    int maxPoolSize = environment.getProperty("app.task-executor.max-pool-size", Integer.class, 10);
    int queueCapacity =
        environment.getProperty("app.task-executor.queue-capacity", Integer.class, 1000);

    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize);
    executor.setMaxPoolSize(maxPoolSize);
    executor.setQueueCapacity(queueCapacity);
    executor.setThreadNamePrefix("bg-task-");
    executor.initialize();
    return executor;
  }
}
