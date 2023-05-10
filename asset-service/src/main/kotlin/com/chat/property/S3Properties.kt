package com.chat.property

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


@Data
@Component
@ConfigurationProperties(prefix = "s3")
class S3Properties {
    lateinit var bucketName: String
    lateinit var region: String
    lateinit var awsId: String
    lateinit var awsKey: String
    lateinit var deleteFileDestinationKey: String
    lateinit var deleteFileDestinationBucket: String
}

