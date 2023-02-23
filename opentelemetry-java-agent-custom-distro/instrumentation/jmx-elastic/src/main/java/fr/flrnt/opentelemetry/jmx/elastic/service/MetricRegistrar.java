package fr.flrnt.opentelemetry.jmx.elastic.service;

import static java.util.logging.Level.INFO;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.metrics.DoubleGaugeBuilder;
import io.opentelemetry.api.metrics.LongCounterBuilder;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableDoubleMeasurement;
import io.opentelemetry.api.metrics.ObservableLongMeasurement;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class MetricRegistrar {

  private static final Logger logger = Logger.getLogger(MetricRegistrar.class.getName());

  private final Meter meter;

  MetricRegistrar(OpenTelemetry openTelemetry, String instrumentationScope) {
    meter = openTelemetry.getMeter(instrumentationScope);
  }

  public void enrollMetric(JmxMetric jmxMetric) {
    logger.info(String.format("enrolling metric %s", jmxMetric.getMetricName()));

    switch (jmxMetric.getMetricType()) {
      case GAUGE:
        {
          DoubleGaugeBuilder builder = meter.gaugeBuilder(jmxMetric.getMetricName());
          builder.buildWithCallback(doubleTypeCallback(jmxMetric));
          logger.log(INFO, "Created UpDownCounter for {0}", jmxMetric.getMetricName());
        }
        break;
      case UPDOWNCOUNTER:
        {
          LongCounterBuilder builder = meter.counterBuilder(jmxMetric.getMetricName());
          builder.buildWithCallback(longTypeCallback(jmxMetric));
          logger.log(INFO, "Created UpDownCounter for {0}", jmxMetric.getMetricName());
        }
        break;
      case COUNTER:
        {
          LongCounterBuilder builder = meter.counterBuilder(jmxMetric.getMetricName());
          builder.buildWithCallback(longTypeCallback(jmxMetric));
          logger.log(INFO, "Created Counter for {0}", jmxMetric.getMetricName());
        }
        break;
    }
  }

  /*
   * A method generating metric collection callback for asynchronous Measurement
   * of Double type.
   */
  static Consumer<ObservableDoubleMeasurement> doubleTypeCallback(JmxMetric jmxMetric) {
    return measurement -> {
      Number metricValue = jmxMetric.getValue().get();
      if (metricValue != null) {
        // get the metric attributes
        Attributes attr = createMetricAttributes(jmxMetric);
        measurement.record(metricValue.doubleValue(), attr);
      }
    };
  }

  /*
   * A method generating metric collection callback for asynchronous Measurement
   * of Long type.
   */
  static Consumer<ObservableLongMeasurement> longTypeCallback(JmxMetric jmxMetric) {
    return measurement -> {
      Number metricValue = jmxMetric.getValue().get();
      if (metricValue != null) {
        // get the metric attributes
        Attributes attr = createMetricAttributes(jmxMetric);
        measurement.record(metricValue.longValue(), attr);
      }
    };
  }

  static Attributes createMetricAttributes(JmxMetric jmxMetric) {
    AttributesBuilder attrBuilder = Attributes.builder();
    var labels = jmxMetric.getLabels();
    labels.forEach(attrBuilder::put);
    return attrBuilder.build();
  }
}
