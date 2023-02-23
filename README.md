## Before running the docker-compose

```
sysctl -w vm.max_map_count=262144
systemctl restart docker
```

Kibana Url : http://localhost:5601

user : elastic
password : elastic

https://www.elastic.co/guide/en/apm/guide/8.6/api-key.html
https://www.elastic.co/guide/en/apm/guide/master/apm-quick-start.html

## Setting up the fleet server and APM endpoint

Start the fleet server. It will register itself to elasticsearch.

```
docker compose up -d fleet-server
```

Connect to Kibana, then install the APM integration by going to http://localhost:5601/app/integrations/detail/apm/overview > Add Elastic APM.

In the configuration screen:
 * in **1 Configure integration > General > Server configuration > Host** : `0.0.0.0:8200`. 
 * in **1 Configure integration > Agent authorization**, fill in a *secret token*. 
 * in **2 Where to add this integration ?** -> Existing Host et select *Fleet Server (APM)*.
Then save and continue, and deploy changes.

Your APM server is now setup. Copy the `.env.template` file to `.env` file, 
and fill in the previously created secret token to `ES_APM_BEARER_TOKEN` && `ES_APM_TOKEN`.

You should be able to easily send APM data, whether with an Elastic Agent or with OpenTelemetry.

You can start the Opentelemetry collector with `docker compose up -d otel-collector`.

## Building and using the exemple app image

When building the example app image, we will be building the additional need jars before and putting them in the image.

Building everything can be done with `make build-all`

## How to

### Export opentelemetry data directly without using opentelemetry collector
```      
OTEL_EXPORTER_OTLP_ENDPOINT: http://fleet-server:8200
OTEL_EXPORTER_OTLP_HEADERS: "Authorization=${ES_APM_BEARER_TOKEN}"
```

### Debug log on opentelemetry agent
```
OTEL_JAVAAGENT_DEBUG: 'true'
```

### Elastic Indices: Dynamic field mapping on apm.internal indices
Stack Management > Index Management > Index Templates > metrics-apm.internal > manage > edit > 6. mappings > Advanced options > Enable dynamic mapping off/on > save

Rollover not necessary, Elasticsearch should do it itself. In any case : 
```
GET /_cat/indices

GET .ds-metrics-apm.internal-default-2023.02.23-000001

POST metrics-apm.internal-default/_rollover/

DELETE .ds-metrics-apm.internal-default-2023.02.23-000001
```

### Setting agregation period for metrics
in kibana.yml:
```
xpack.apm.metricsInterval: {A value higher than the opentelemetry metrics export value (wihich default to 60)
```

### Lower Opentelemetry metrics export interval
```
OTEL_METRIC_EXPORT_INTERVAL: 30000
```