package lake;

import java.util.Properties;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class App {

    public static void main(String[] args) {

        SparkSession spark = SparkSession.builder()
                .appName("Lakehouse-Pipeline")
                .master("local[*]")

                // Nessie
                .config("spark.sql.catalog.nessie",
                        "org.apache.iceberg.spark.SparkCatalog")
                .config("spark.sql.catalog.nessie.uri",
                        "http://nessie:19120/api/v2")
                .config("spark.sql.catalog.nessie.ref",
                        "main")
                .config("spark.sql.catalog.nessie.authentication.type",
                        "NONE")
                .config("spark.sql.catalog.nessie.catalog-impl",
                        "org.apache.iceberg.nessie.NessieCatalog")

                // Iceberg
                .config("spark.sql.catalog.nessie.warehouse",
                        "s3://warehouse/")
                .config("spark.sql.catalog.nessie.io-impl",
                        "org.apache.iceberg.aws.s3.S3FileIO")

                // MinIO
                .config("spark.sql.catalog.nessie.s3.endpoint",
                        "http://minio:9000")
                .config("spark.sql.catalog.nessie.s3.access-key-id",
                        "admin")
                .config("spark.sql.catalog.nessie.s3.secret-access-key",
                        "password")
                .config("spark.sql.catalog.nessie.s3.path-style-access",
                        "true")

                // Spark extensions
                .config("spark.sql.extensions",
                        "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions,"
                                + "org.projectnessie.spark.extensions.NessieSparkSessionExtensions")

                .getOrCreate();

        spark.sparkContext().setLogLevel("WARN");

        // PostgreSQL connection
        String jdbcUrl = "jdbc:postgresql://postgres:5432/demo";

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");
        properties.setProperty("driver", "org.postgresql.Driver");

        // Read data from PostgreSQL
        Dataset<Row> salesDf = spark.read()
                .jdbc(jdbcUrl, "fashion_sales", properties);

        System.out.println("Data from PostgreSQL:");
        salesDf.show(10, false);

        // Create Iceberg namespace
        spark.sql("CREATE NAMESPACE IF NOT EXISTS nessie.sales");

        // Write to Iceberg table
        salesDf.writeTo("nessie.sales.fashion_sales")
                .createOrReplace();

        // Verify data was written
        Dataset<Row> icebergTable = spark.read()
                .table("nessie.sales.fashion_sales");

        System.out.println("Data from Iceberg:");
        icebergTable.show(10, false);

        spark.stop();
    }
}