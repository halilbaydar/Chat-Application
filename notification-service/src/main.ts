import {NestFactory} from '@nestjs/core';
import {AppModule} from './app.module';
import * as express from 'express'
import * as http from "http";
import {INestMicroservice} from "@nestjs/common";
import {MicroserviceOptions} from "@nestjs/microservices";
import {KafkaConsumerConfig} from "./modules/kafka/config/kafka.consumer.config";

class NotificationServiceApplication {
    static async run(): Promise<INestMicroservice> {
        const server = express()
        const app = await NestFactory
            .createMicroservice<MicroserviceOptions>(AppModule, KafkaConsumerConfig);

        http.createServer(server).listen(process.env.PORT, () => {
            console.log(`🚀 app listening on port ${process.env.PORT}!`);
        })
        return await app.init()
    }
}

NotificationServiceApplication
    .run()
    .then(application => {
        console.info("Notification Service up and running")
    }).catch(err => {
    console.error("Notification Service is not started")
})
