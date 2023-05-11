import {MicroserviceOptions} from "@nestjs/microservices/interfaces/microservice-configuration.interface";
import {Transport} from "@nestjs/microservices";

export const KafkaConsumerConfig: MicroserviceOptions = {
    transport: Transport.KAFKA,
    options: {
        client: {
            brokers: [process.env.KAFKA_BROKERS]
        }, consumer: {
            groupId: process.env.KAFKA_GROUP_ID
        }
    }
}