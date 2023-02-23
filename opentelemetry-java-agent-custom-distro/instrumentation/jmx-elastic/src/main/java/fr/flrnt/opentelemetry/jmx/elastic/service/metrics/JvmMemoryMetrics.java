package fr.flrnt.opentelemetry.jmx.elastic.service.metrics;

import fr.flrnt.opentelemetry.jmx.elastic.service.JmxMetric;
import fr.flrnt.opentelemetry.jmx.elastic.service.MetricRegistrar;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;

public class JvmMemoryMetrics {

  public static void registerMetrics(MetricRegistrar metricRegistrar) {
    final MemoryMXBean platformMXBean = ManagementFactory.getPlatformMXBean(MemoryMXBean.class);

    metricRegistrar.enrollMetric(
        JmxMetric.upDownCounterJmxMetric(
            "jvm.memory.heap.used", () -> platformMXBean.getHeapMemoryUsage().getUsed(), null));
    metricRegistrar.enrollMetric(
        JmxMetric.upDownCounterJmxMetric(
            "jvm.memory.heap.committed",
            () -> platformMXBean.getHeapMemoryUsage().getCommitted(),
            null));
    metricRegistrar.enrollMetric(
        JmxMetric.upDownCounterJmxMetric(
            "jvm.memory.heap.max", () -> platformMXBean.getHeapMemoryUsage().getMax(), null));
    metricRegistrar.enrollMetric(
        JmxMetric.upDownCounterJmxMetric(
            "jvm.memory.non_heap.used",
            () -> platformMXBean.getNonHeapMemoryUsage().getUsed(),
            null));
    metricRegistrar.enrollMetric(
        JmxMetric.upDownCounterJmxMetric(
            "jvm.memory.non_heap.committed",
            () -> platformMXBean.getNonHeapMemoryUsage().getUsed(),
            null));
    metricRegistrar.enrollMetric(
        JmxMetric.upDownCounterJmxMetric(
            "jvm.memory.non_heap.max",
            () -> platformMXBean.getNonHeapMemoryUsage().getUsed(),
            null));

    List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

    //    for (final MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
    //      if (memoryPoolMXBean.getType() != MemoryType.HEAP) {
    //        continue;
    //      }
    //      // TODO labels ?
    //      metricRegistrar.enrollMetric(
    //          JmxMetric.upDownCounterJmxMetric(
    //              "tom.memory.non_heap.max", () ->
    // platformMXBean.getNonHeapMemoryUsage().getUsed()));
    //
    //    }
  }
}
