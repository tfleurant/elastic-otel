FROM eclipse-temurin:17-jdk as builder
ARG JAR_FILE=build/libs/api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:17-jdk
COPY --from=builder dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./

ADD ./external/jmx-extension.jar /thirdparty/
ADD ./external/agent-distro.jar /thirdparty/
ADD https://search.maven.org/remotecontent?filepath=co/elastic/apm/elastic-apm-agent/1.36.0/elastic-apm-agent-1.36.0.jar /thirdparty/elastic-apm-agent.jar
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.22.1/opentelemetry-javaagent.jar /thirdparty/

#ENTRYPOINT ["java","--enable-preview","-javaagent:thirdparty/opentelemetry-javaagent.jar","org.springframework.boot.loader.JarLauncher"]
#ENTRYPOINT java --enable-preview -javaagent:thirdparty/elastic-apm-agent.jar -Delastic.apm.service_name=example-app-with-elastic -Delastic.apm.application_packages=com.example -Delastic.apm.server_url=http://fleet-server:8200 org.springframework.boot.loader.JarLauncher

# OTEL java agent only
#ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.22.1/opentelemetry-javaagent.jar /thirdparty/
#ENTRYPOINT ["java","--enable-preview","-javaagent:thirdparty/opentelemetry-javaagent.jar","org.springframework.boot.loader.JarLauncher"]

# OTEL java agent + extension
#ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.22.1/opentelemetry-javaagent.jar /thirdparty/
#ADD ./external/jmx-extension.jar /thirdparty/
#ENTRYPOINT ["java","--enable-preview","-javaagent:thirdparty/opentelemetry-javaagent.jar", "-Dotel.javaagent.extensions=thirdparty/jmx-extension.jar", "org.springframework.boot.loader.JarLauncher"]

# Packaging distro : OK
#ADD ./external/agent-distro.jar /thirdparty/
#ENTRYPOINT ["java","--enable-preview","-javaagent:thirdparty/agent-1.0-SNAPSHOT-all.jar", "org.springframework.boot.loader.JarLauncher"]

# Packaging distro + elastic : OK
#ADD ./external/agent-distro.jar /thirdparty/
#ADD https://search.maven.org/remotecontent?filepath=co/elastic/apm/elastic-apm-agent/1.36.0/elastic-apm-agent-1.36.0.jar /thirdparty/elastic-apm-agent.jar
#ENTRYPOINT ["java","--enable-preview","-javaagent:thirdparty/agent-distro.jar", "-javaagent:thirdparty/elastic-apm-agent.jar", "-Delastic.apm.service_name=example-app-with-elastic", "-Delastic.apm.application_packages=com.example", "-Delastic.apm.server_url=http://fleet-server:8200", "org.springframework.boot.loader.JarLauncher"]

ENTRYPOINT ["java", "-javaagent:thirdparty/elastic-apm-agent.jar", "-Delastic.apm.service_name=app-through-elastic-agent", "-Delastic.apm.application_packages=com.example", "-Delastic.apm.server_url=http://fleet-server:8200", "org.springframework.boot.loader.JarLauncher"]