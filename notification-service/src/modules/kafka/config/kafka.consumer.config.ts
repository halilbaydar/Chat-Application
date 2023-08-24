import {KafkaOptions, MicroserviceOptions} from "@nestjs/microservices/interfaces/microservice-configuration.interface";
import {Transport} from "@nestjs/microservices";
import {CLIENT_MODULE_NAME} from "../kafka.module";
import {ClientProviderOptions} from "@nestjs/microservices/module/interfaces/clients-module.interface";

export const KafkaConsumerConfig: KafkaOptions = {
    transport: Transport.KAFKA,
    options: {
        client: {
            clientId: CLIENT_MODULE_NAME,
            brokers: ["localhost:19092", "localhost:29092", "localhost:39092"],
        },
        consumer: {
            groupId: process.env.KAFKA_GROUP_ID
        },
        subscribe: {
            fromBeginning: true,
        },
    },
}