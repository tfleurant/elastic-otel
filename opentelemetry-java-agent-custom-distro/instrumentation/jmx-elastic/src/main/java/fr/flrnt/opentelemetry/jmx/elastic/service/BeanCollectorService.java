package fr.flrnt.opentelemetry.jmx.elastic.service;

import fr.flrnt.opentelemetry.jmx.elastic.service.metrics.JvmGcMetrics;
import fr.flrnt.opentelemetry.jmx.elastic.service.metrics.JvmMemoryMetrics;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BeanCollectorService {

  private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

  private final MetricRegistrar registrar;

  private final long discoveryDelay;
  private final long maxDelay;

  private long delay = 1000; // number of milliseconds until first attempt to discover MBeans

  public BeanCollectorService(MetricRegistrar registrar, long discoveryDelay) {
    this.registrar = registrar;
    this.discoveryDelay = Math.max(1000, discoveryDelay); // Enforce sanity
    this.maxDelay = Math.max(60000, discoveryDelay);
  }

  void discoverBeans() {

    exec.schedule(
        new Runnable() {
          @Override
          public void run() {
            enrollMetrics();
            // Use discoveryDelay as the increment for the actual delay
            //                        delay = Math.min(delay + discoveryDelay, maxDelay);
            //                        exec.schedule(this, delay, TimeUnit.MILLISECONDS);
          }
        },
        delay,
        TimeUnit.MILLISECONDS);
  }

  private void enrollMetrics() {
    JvmMemoryMetrics.registerMetrics(registrar);
    JvmGcMetrics.registerMetrics(registrar);
  }
}
