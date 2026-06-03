## From PostgreSQL to Analytics: Designing a Lakehouse with Spark, Iceberg, Nessie, and Dremio


This project demonstrates an end-to-end Lakehouse architecture built entirely with open-source technologies.

A Spark application extracts data from PostgreSQL, writes it as Apache Iceberg tables to MinIO object storage, and registers metadata in Nessie. Dremio queries the Iceberg tables directly and exposes them to downstream analytics and BI tool Apache Superset.



#### Architecture Diagram

<img width="1257" height="379" alt="lkh" src="https://github.com/user-attachments/assets/9921772c-0b21-40ce-a6f4-ecd8fecce92e" />



#### Start the Environment


Build the containers:

```sh
docker compose build --no-cache
```
Start all services:

```sh
docker compose up -d
```

Verify all services are running:

```sh
docker compose ps
```

#### Load Sample Data into PostgreSQL

Connect to PostgreSQL:

```sh
docker compose exec postgres psql -U root -d demo
```

> Execute the SQL statements contained in init.sql

#### Build the Spark Application

Package the application:

```sh
mvn clean package -DskipTests
```

Copy the generated JAR into the Spark container:

```sh
docker cp \
target/ice-1.0-SNAPSHOT.jar \
spark-master:/opt/spark/
```

#### Run the Spark Ingestion Job

Access the Spark container:

```sh
docker compose exec -it spark-master bash
```

Submit the job:

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


#### Verify Iceberg Files in MinIO

Configure the MinIO client:
```sh
mc alias set local http://localhost:9000 admin password
```

List Iceberg table files:
```sh
mc ls local/warehouse/sales
```
> You should see Iceberg metadata and Parquet data files.


#### Configure Dremio

When adding MinIO as an S3 source in Dremio, use the following properties:

```sh
    fs.s3a.endpoint= minio:9000
    fs.s3a.path.style.access=true
    dremio.s3.compat=true
```

After configuring Nessie and MinIO in Dremio, run:

```sh
SELECT * FROM nessie.sales.fashion_sales;
```


#### Connect Apache Superset

Create a new database connection using Dremio Flight SQL:

```sh
dremio+flight://test:12345678A@dremio:32010/?UseEncryption=false
```




