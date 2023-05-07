package com.chat.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class S3Client {

    @Value("${s3.accesskey}")
    private String awsId;
    @Value("${s3.secretkey}")
    private String awsKey;
    @Value("${s3.region}")
    private String region;

    @Bean
    public AmazonS3 getS3client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
        return s3Client;
    }
}
