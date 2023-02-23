package fr.flrnt.opentelemetry.jmx.elastic;

import com.google.auto.service.AutoService;
import fr.flrnt.opentelemetry.jmx.elastic.service.JmxService;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.javaagent.extension.AgentListener;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;

@AutoService(AgentListener.class)
public final class JmxElasticMetricsInstaller implements AgentListener {

  @Override
  public void afterAgent(AutoConfiguredOpenTelemetrySdk autoConfiguredOpenTelemetrySdk) {
    JmxService service =
        JmxService.createService(
            GlobalOpenTelemetry.get(),
            autoConfiguredOpenTelemetrySdk
                .getConfig()
                .getLong("otel.metric.export.interval", 60000));
    service.start();
  }
}
