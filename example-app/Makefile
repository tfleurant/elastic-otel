all: build-app build-docker

build-app:
	./gradlew build

build-docker:
	docker build -t elastic-otel/app:latest --no-cache .