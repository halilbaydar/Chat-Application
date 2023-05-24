package com.chat.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component


@Component
class S3Client {
    @Value("\${s3.accesskey}")
    private val awsId: String? = null

    @Value("\${s3.secretkey}")
    private val awsKey: String? = null

    @Value("\${s3.region}")
    private val region: String? = null

    @get:Bean
    val s3client: AmazonS3
        get() {
            val awsCreds = BasicAWSCredentials(awsId, awsKey)
            return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(AWSStaticCredentialsProvider(awsCreds))
                .build()
        }
}
