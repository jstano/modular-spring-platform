package com.stano.spring_boot_application.logging;

import ch.qos.logback.classic.LoggerContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLoggingContextInitializer {
  public static void initialize() {
    Logger logger = LoggerFactory.getLogger(DefaultLoggingContextInitializer.class);

    if (logger instanceof ch.qos.logback.classic.Logger) {
      LoggerContext loggerContext = ((ch.qos.logback.classic.Logger) logger).getLoggerContext();

      if (loggerContext != null && loggerContext.getProperty("HOSTNAME") == null) {
        loggerContext.putProperty("HOSTNAME", getHostName());
      }
    }
  }

  private static String getHostName() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException x) {
      return "UNKNOWN_HOST";
    }
  }
}
