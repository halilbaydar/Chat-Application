package com.chat;

//import com.chat.client.KafkaAdminClient;
//import com.chat.kafka.producer.ElasticKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = "com.chat")
public class UserServiceApplication implements CommandLineRunner {
//    private final KafkaAdminClient kafkaAdminClient;
//    private final ElasticKafkaProducer elasticKafkaProducer;

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
//        kafkaAdminClient.createTopics();
//        kafkaAdminClient.checkSchemaRegistry();
//        elasticKafkaProducer.run();
    }
}
