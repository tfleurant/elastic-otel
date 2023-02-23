package fr.flrnt.opentelemetry.jmx.elastic.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public abstract class JmxMetric {
  private final String metricName;
  private final MetricType metricType;

  private final Map<String, String> labels;

  public JmxMetric(String metricName, MetricType metricType, @Nullable Map<String, String> labels) {
    this.metricName = metricName;
    this.metricType = metricType;
    this.labels = Optional.ofNullable(labels).orElse(Collections.emptyMap());
  }

  abstract Supplier<Number> getValue();

  public String getMetricName() {
    return metricName;
  }

  public MetricType getMetricType() {
    return metricType;
  }

  public Map<String, String> getLabels() {
    return labels;
  }

  public static JmxMetric gaugeJmxMetric(
      String metricName, Supplier<Number> valueSupplier, @Nullable Map<String, String> labels) {
    return new JmxMetric(metricName, MetricType.GAUGE, labels) {
      @Override
      Supplier<Number> getValue() {
        return valueSupplier;
      }
    };
  }

  public static JmxMetric upDownCounterJmxMetric(
      String metricName, Supplier<Number> valueSupplier, @Nullable Map<String, String> labels) {
    return new JmxMetric(metricName, MetricType.UPDOWNCOUNTER, labels) {
      @Override
      Supplier<Number> getValue() {
        return valueSupplier;
      }
    };
  }

  public static JmxMetric counterJmxMetric(
      String metricName, Supplier<Number> valueSupplier, @Nullable Map<String, String> labels) {
    return new JmxMetric(metricName, MetricType.COUNTER, labels) {
      @Override
      Supplier<Number> getValue() {
        return valueSupplier;
      }
    };
  }

  public static JmxMetric cumulativePositiveCounterJmxMetric(
      String metricName, Supplier<Number> valueSupplier, @Nullable Map<String, String> labels) {
    return new JmxMetric(metricName, MetricType.COUNTER, labels) {

      private Long counter = 0L;

      @Override
      Supplier<Number> getValue() {
        return () -> {
          long providedValue = valueSupplier.get().longValue();
          if (providedValue >= 0) {
            counter += providedValue;
          }

          return counter;
        };
      }
    };
  }

  public static JmxMetric positiveCounterJmxMetric(
      String metricName, Supplier<Number> valueSupplier, @Nullable Map<String, String> labels) {
    return new JmxMetric(metricName, MetricType.COUNTER, labels) {

      @Override
      Supplier<Number> getValue() {
        return () -> {
          long providedValue = valueSupplier.get().longValue();
          return providedValue >= 0 ? providedValue : 0;
        };
      }
    };
  }
}
