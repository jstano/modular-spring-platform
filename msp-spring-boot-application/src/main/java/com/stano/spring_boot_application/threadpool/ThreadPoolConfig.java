package com.stano.spring_boot_application.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {
  @Bean
  public TaskExecutor taskExecutor(Environment environment) {
    int corePoolSize = environment.getProperty("app.task-executor.core-pool-size", Integer.class, 4);
    int maxPoolSize = environment.getProperty("app.task-executor.max-pool-size", Integer.class, 10);
    int queueCapacity = environment.getProperty("app.task-executor.queue-capacity", Integer.class, 1000);

    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize);
    executor.setMaxPoolSize(maxPoolSize);
    executor.setQueueCapacity(queueCapacity);
    executor.setThreadNamePrefix("bg-task-");
    executor.initialize();
    return executor;
  }
}
