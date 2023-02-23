build-extension:
	$(MAKE) -C opentelemetry-java-agent-extension build
	mkdir -p example-app/external
	cp ./opentelemetry-java-agent-extension/build/libs/opentelemetry-jmx-jvm-extension-1.0-SNAPSHOT.jar example-app/external/jmx-extension.jar

build-distro:
	$(MAKE) -C opentelemetry-java-agent-custom-distro build
	mkdir -p example-app/external
	cp ./opentelemetry-java-agent-custom-distro/agent/build/libs/agent-1.0-SNAPSHOT-all.jar example-app/external/agent-distro.jar

build-app: build-extension build-distro
	$(MAKE) -C example-app all

fleet:
	docker compose up -d fleet-server

collector:
	docker compose up -d otel-collector

app:
	docker compose up app