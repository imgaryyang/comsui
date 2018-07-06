package com.suidifu.dowjones.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/10/25 <br>
 * @time: 下午10:56 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Configuration
@Slf4j
public class SparkConfig {
    @Resource
    private SparkProperties sparkProperties;

    @Bean
    public SparkSession sparkSession() {
        log.info("\nApplicationName is:{}\nmaster uri is:{}\n",
                sparkProperties.getApplicationName(),
                sparkProperties.getMasterURI());

        return SparkSession.builder()
                .config("spark.port.maxRetries", "100")
//                .config("spark.testing.memory", "2147480000")
                .config("spark.sql.codegen", true)
                .config("spark.sql.inMemoryColumnStorage.compressed", true)
                .config("spark.sql.inMemoryColumnStorage.batchSize", "5000")
                .config("spark.sql.files.maxPartitionBytes", "268435456")//256MB
                .config("spark.sql.shuffle.partitions", "20")
                .config("spark.sql.parquet.compressed.codec", "gzip")
                .config("spark.driver.memory", "14g")
                .config("spark.driver.cores", 8)
                .config("spark.driver.maxResultSize","12g")
                .config("spark.executor.memory", "12g")
                .config("spark.storage.memoryFraction", "0.2")
                .config("spark.worker.memory", "12g")
//                .config("spark.locality.wait", "180s")
                .config("spark.worker.cleanup.enabled", true)
                .config("spark.worker.cleanup.interval", 900)
                .config("spark.worker.cleanup.appDataTtl", 60)
                .master(sparkProperties.getMasterURI())
                .appName(sparkProperties.getApplicationName())
                .getOrCreate();
    }
}