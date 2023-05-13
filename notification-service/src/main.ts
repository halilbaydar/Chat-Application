import {NestFactory} from '@nestjs/core';
import {AppModule} from './app.module';
import express from 'express';
import * as http from "http";
import {INestMicroservice} from "@nestjs/common";
import {MicroserviceOptions} from "@nestjs/microservices";
import {KafkaConsumerConfig} from "./modules/kafka/config/kafka.consumer.config";
import process from 'node:process';
import cluster from 'node:cluster';
import {availableParallelism} from 'node:os';

const numCPUs = availableParallelism();

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

if (cluster.isPrimary) {
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
