package fr.flrnt.opentelemetry.jmx.elastic.service;

import io.opentelemetry.api.OpenTelemetry;

import java.util.logging.Logger;

public class JmxService {
  private static final Logger logger = Logger.getLogger(JmxService.class.getName());

  private static final String INSTRUMENTATION_SCOPE = "flrnt.opentelemetry.jmx.fr.flrnt.opentelemetry.jmx.elastic.elastic";

  private final OpenTelemetry openTelemetry;
  private final long discoveryDelay;

  public static JmxService createService(OpenTelemetry openTelemetry, long discoveryDelay) {
    return new JmxService(openTelemetry, discoveryDelay);
  }

  private JmxService(OpenTelemetry openTelemetry, long discoveryDelay) {
    this.openTelemetry = openTelemetry;
    this.discoveryDelay = discoveryDelay;
  }

  public static Logger getLogger() {
    return logger;
  }

  public void start() {
    MetricRegistrar registry = new MetricRegistrar(openTelemetry, INSTRUMENTATION_SCOPE);
    BeanCollectorService collectorService = new BeanCollectorService(registry, this.discoveryDelay);
    collectorService.discoverBeans();
  }
}
