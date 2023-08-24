import {NestFactory} from '@nestjs/core';
import {AppModule} from './app.module';
import express from 'express';
import * as http from "http";
import {INestMicroservice} from "@nestjs/common";
import process from 'node:process';
import cluster from 'node:cluster';
import {availableParallelism} from 'node:os';
import {KafkaOptions} from "@nestjs/microservices/interfaces/microservice-configuration.interface";
import {Transport} from "@nestjs/microservices";
import {CLIENT_MODULE_NAME} from "./modules/kafka/kafka.module";
import {ExpressAdapter} from "@nestjs/platform-express";
import {CustomDeserializer} from "./modules/kafka/parser/kafka.deserializer";
// require("util").inspect.defaultOptions.depth = null;

const numCPUs = availableParallelism();

class NotificationServiceApplication {
    static async run(): Promise<void> {
        const server = express()
        const app = await NestFactory.createMicroservice<KafkaOptions>(AppModule, {
            transport: Transport.KAFKA,
            options: {
                // deserializer: new CustomDeserializer(),
                run: {
                    autoCommit: true,
                },
                subscribe: {
                    fromBeginning: true
                },
                consumer: {
                    groupId: process.env.KAFKA_GROUP_ID,
                },
                client: {
                    clientId: CLIENT_MODULE_NAME,
                    brokers: ["localhost:19092", "localhost:29092", "localhost:39092"],
                }
            }
        });

       await app.listen()
    }
}

if (cluster.isPrimary && process.env.CLUSTER_MODE_ENABLED) {
    console.log(`Primary ${process.pid} is running`);

    // Fork workers.
    for (let i = 0; i < numCPUs; i++) {
        cluster.fork();
    }

    cluster.on('exit', (worker, code, signal) => {
        console.log(`worker ${worker.process.pid} died`);
    });
} else {
    // Workers can share any TCP connection
    // In this case it is an HTTP server
    NotificationServiceApplication
        .run()
        .then(application => {
            console.info("Notification Service up and running")
        }).catch(err => {
        console.error("Notification Service is not started", err)
    })

    console.log(`Worker ${process.pid} started`);
}
