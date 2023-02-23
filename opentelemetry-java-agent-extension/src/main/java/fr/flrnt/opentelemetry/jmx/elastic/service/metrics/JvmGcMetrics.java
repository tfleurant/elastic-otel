package fr.flrnt.opentelemetry.jmx.elastic.service.metrics;

import fr.flrnt.opentelemetry.jmx.elastic.service.JmxMetric;
import fr.flrnt.opentelemetry.jmx.elastic.service.MetricRegistrar;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;

public class JvmGcMetrics {

  public static void registerMetrics(MetricRegistrar metricRegistrar) {
    final List<GarbageCollectorMXBean> garbageCollectorMXBeans =
        ManagementFactory.getGarbageCollectorMXBeans();

    for (final GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {
      metricRegistrar.enrollMetric(
          JmxMetric.positiveCounterJmxMetric(
              "jvm.gc.count",
              garbageCollectorMXBean::getCollectionCount,
              Map.of("name", garbageCollectorMXBean.getName())));
      metricRegistrar.enrollMetric(
          JmxMetric.positiveCounterJmxMetric(
              "jvm.gc.time",
              garbageCollectorMXBean::getCollectionTime,
              Map.of("name", garbageCollectorMXBean.getName())));
    }

    // TODO ThreadMxBean ?
  }
}
