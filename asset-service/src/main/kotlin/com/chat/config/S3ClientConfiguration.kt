package com.chat.config

import com.chat.property.S3Properties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.http.async.SdkAsyncHttpClient
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder
import software.amazon.awssdk.services.s3.S3Configuration
import java.time.Duration


@Configuration
@EnableConfigurationProperties(S3Properties::class)
class S3ClientConfiguration(private val s3Properties: S3Properties) {

    @Bean
    fun awsCredentialsProvider(): AwsCredentialsProvider {
        return AwsCredentialsProvider {
            AwsBasicCredentials.create(s3Properties.awsId, s3Properties.awsKey)
        }
    }

    @Bean
    fun s3client(credentialsProvider: AwsCredentialsProvider): S3AsyncClient {
        val httpClient: SdkAsyncHttpClient = NettyNioAsyncHttpClient.builder()
            .writeTimeout(Duration.ZERO)
            .maxConcurrency(64)
            .build()
        val serviceConfiguration: S3Configuration = S3Configuration.builder()
            .checksumValidationEnabled(false)
            .chunkedEncodingEnabled(true)
            .build()
        val b: S3AsyncClientBuilder = S3AsyncClient.builder().httpClient(httpClient)
            .region(Region.of(s3Properties.region))
            .credentialsProvider(credentialsProvider)
            .serviceConfiguration(serviceConfiguration)
        return b.build()
    }
}