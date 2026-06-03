## From PostgreSQL to Analytics: Designing a Lakehouse with Spark, Iceberg, Nessie, and Dremio

### Architecture Diagram


```sh
docker compose exec postgres psql -U root -d demo
```

```sh
docker cp \
target/ice-1.0-SNAPSHOT.jar \
spark-master:/opt/spark/
```


```sh
/opt/spark/bin/spark-submit \
  --class lake.App \
  --conf spark.jars.ivy=/tmp/.ivy2 \
  --packages \
org.postgresql:postgresql:42.7.3,\
org.apache.iceberg:iceberg-spark-runtime-3.5_2.12:1.5.0,\
org.projectnessie.nessie-integrations:nessie-spark-extensions-3.5_2.12:0.77.1,\
software.amazon.awssdk:bundle:2.24.8,\
software.amazon.awssdk:url-connection-client:2.24.8 \
/opt/spark/ice-1.0-SNAPSHOT.jar
```


```sh

```


```sh
mc alias set local http://localhost:9000 admin password
```

```sh
mc ls local/warehouse/sales
```



```sh
docker compose exec -it spark-master  bash
```




```sh
dremio+flight://test:12345678A@dremio:32010/?UseEncryption=false
```

```sh
SELECT * FROM nessie.sales.fashion_sales;
```

```sh
    fs.s3a.endpoint= minio:9000
    fs.s3a.path.style.access=true
    dremio.s3.compat=true
```