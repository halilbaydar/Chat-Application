import {NestFactory} from '@nestjs/core';
import {AppModule} from './app.module';
import * as express from 'express'
import {ExpressAdapter} from "@nestjs/platform-express";
import * as http from "http";

async function bootstrap() {
    const server = express()
    const app = await NestFactory.create(AppModule, new ExpressAdapter(server));

    http.createServer(server).listen(process.env.PORT, () => {
        console.log(`🚀 app listening on port ${process.env.PORT}!`);
    })
    await app.init()
}

bootstrap();
