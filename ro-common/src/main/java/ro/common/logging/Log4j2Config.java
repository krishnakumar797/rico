package ro.common.logging;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import ro.common.exception.LogInitException;
import ro.common.utils.Utils;
import ro.common.utils.Utils.LOG_APPENDERS;
import ro.common.utils.Utils.LOG_LEVELS;

/**
 * Default configuration class for Log4j2
 *
 * @author r.krishnakumar
 */
@Configuration
public class Log4j2Config {

  @Value("#{${ro.logging.appPackages:{'': 'INFO'}}}")
  private Map<String, String> appPackages;

  @Value("${ro.logging.supressStackTrace:false}")
  private boolean supressStackTrace;

  @Value("${ro.logging.rootLogger:false}")
  private boolean rootLogger;

  @Value("${ro.logging.rootLogger.logLevel:INFO}")
  private String logLevel;

  @Value("${ro.logging.writeTo:CONSOLE}")
  private String appender;

  private static final String CONSOLE_APPENDER_NAME = "Stdout";

  private static final String ROLLINGFILE_APPENDER_NAME = "rolling";

  private static final String LOG4J_PACKAGE_NAME = "org.apache.logging.log4j";

  @PostConstruct
  private void initialize() {
    System.out.println("INIT LOG4j2");

    LOG_APPENDERS appenders = Utils.LOG_APPENDERS.value(appender);
    if (appenders == null) {
      throw new LogInitException(
          "Unknown log appender found "
              + this.appender
              + ". Supported appenders are CONSOLE, FILE");
    }
    appPackages.forEach(
        (packageName, logLevel) -> {
          if (packageName.isEmpty()) {
            return;
          }
          LOG_LEVELS levels = Utils.LOG_LEVELS.value(logLevel);
          if (levels == null) {
            throw new LogInitException(
                "Unknown log level found "
                    + logLevel
                    + ". Supported levels are TRACE, DEBUG, INFO, WARN, ERROR, FATAL");
          }
        });
    // Check whether rootlogger loglevel is valid
    if (rootLogger) {
      LOG_LEVELS levels = Utils.LOG_LEVELS.value(logLevel);
      if (levels == null) {
        throw new LogInitException(
            "Unknown rootLogger log level found "
                + logLevel
                + ". Supported levels are TRACE, DEBUG, INFO, WARN, ERROR, FATAL");
      }
    }
    createConfiguration();
  }

  /**
   * Method to create the programmatic configuration for log4j. New loggers will be created against
   * application root package names and any logging outside the the declared packages will be
   * handled by the spring boot default logger
   *
   * @param name
   * @param builder
   * @return
   */
  private void createConfiguration() {

    final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);

    ConfigurationBuilder<BuiltConfiguration> builder =
        ConfigurationBuilderFactory.newConfigurationBuilder();

    builder.setConfigurationName("Rico");
    // Log level for the internal log4j2 events
    builder.setStatusLevel(Level.ERROR);

    LayoutComponentBuilder layoutBuilder = null;
    if (supressStackTrace) {
      layoutBuilder =
          builder
              .newLayout("PatternLayout")
              .addAttribute(
                  "pattern",
                  "%d{yyyy-MM-dd HH:mm:ss.SSS} %clr{%-5level} %clr{%pid} %clr{---} %clr{[%15.15t]} %clr{%-40.40c{-1}} %clr{:} %msg%throwable{short.message}%n");
    } else {
      layoutBuilder =
          builder
              .newLayout("PatternLayout")
              .addAttribute(
                  "pattern",
                  "%d{yyyy-MM-dd HH:mm:ss.SSS} %clr{%-5level} %clr{%pid} %clr{---} %clr{[%15.15t]} %clr{%-40.40c{-1}} %clr{:} %msg%throwable%n");
    }
    AppenderComponentBuilder appenderBuilder = null;

    switch (Utils.LOG_APPENDERS.value(appender)) {
      case CONSOLE:
        // Console appender
        appenderBuilder =
            builder
                .newAppender(CONSOLE_APPENDER_NAME, "CONSOLE")
                .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT)
                .add(layoutBuilder);
        builder.add(appenderBuilder);
        break;
      case FILE:
        // Create rolling file appender
        ComponentBuilder<?> triggeringPolicy =
            builder
                .newComponent("Policies")
                .addComponent(
                    builder
                        .newComponent("CronTriggeringPolicy")
                        .addAttribute("schedule", "0 0 0 * * ?"))
                .addComponent(
                    builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "10M"));
        appenderBuilder =
            builder
                .newAppender(ROLLINGFILE_APPENDER_NAME, "RollingFile")
                .addAttribute("fileName", "logs/log.log")
                .addAttribute("filePattern", "logs/log-%d{MM-dd-yy}.log.gz")
                .add(layoutBuilder)
                .addComponent(triggeringPolicy);
        builder.add(appenderBuilder);
        break;
      default:
        break;
    }

    // Configuring logger with appender for application root packages
    appPackages.forEach(
        (packageName, logLevel) -> {
          if (packageName.isEmpty()) {
            return;
          }
          createLogger(builder, packageName, logLevel);
        });

    // Configuring logger with appender for log4j
    createLogger(builder, LOG4J_PACKAGE_NAME, Utils.LOG_LEVELS.ERROR.name());

    BuiltConfiguration configuration = builder.build();

    // Starting log appenders based on the appender configuration
    switch (Utils.LOG_APPENDERS.value(appender)) {
      case CONSOLE:
        // Console logger
        configuration.getAppender(CONSOLE_APPENDER_NAME).start();
        // Check if the rootLogger is enabled
        if (rootLogger) {
          ctx.getConfiguration().getRootLogger().removeAppender("Console");
          ctx.getConfiguration()
              .getRootLogger()
              .addAppender(
                  configuration.getAppender(CONSOLE_APPENDER_NAME), Level.toLevel(logLevel), null);
        }
        break;
      case FILE:
        // Rolling file logger
        configuration.getAppender(ROLLINGFILE_APPENDER_NAME).start();
        // Check if the rootLogger is enabled
        if (rootLogger) {
          ctx.getConfiguration().getRootLogger().removeAppender("Console");
          ctx.getConfiguration()
              .getRootLogger()
              .addAppender(
                  configuration.getAppender(ROLLINGFILE_APPENDER_NAME),
                  Level.toLevel(logLevel),
                  null);
        }
        break;
      default:
        break;
    }

    // Adding package level loggers to the default log4j context
    appPackages.forEach(
        (packageName, logLevel) -> {
          if (packageName.isEmpty()) {
            return;
          }
          ctx.getConfiguration().addLogger(packageName, configuration.getLogger(packageName));
        });

    ctx.getConfiguration()
        .addLogger(LOG4J_PACKAGE_NAME, configuration.getLogger(LOG4J_PACKAGE_NAME));

    ctx.updateLoggers();
  }

  /**
   * Method to create loggers for packages
   *
   * @param builder
   * @param packageName
   * @param logLevel
   */
  private void createLogger(
      ConfigurationBuilder<BuiltConfiguration> builder, String packageName, String logLevel) {
    switch (Utils.LOG_APPENDERS.value(appender)) {
      case CONSOLE:
        // Console logger
        builder.add(
            builder
                .newLogger(packageName, Level.toLevel(logLevel))
                .add(builder.newAppenderRef(CONSOLE_APPENDER_NAME))
                .addAttribute("additivity", false));
        break;
      case FILE:
        // Rolling file logger
        builder.add(
            builder
                .newLogger(packageName, Level.toLevel(logLevel))
                .add(builder.newAppenderRef(ROLLINGFILE_APPENDER_NAME))
                .addAttribute("additivity", false));
        break;
      default:
        break;
    }
  }
}
