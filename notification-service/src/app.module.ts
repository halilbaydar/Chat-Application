import {Module} from '@nestjs/common';
import {AppController} from './app.controller';
import {AppService} from './app.service';
import {ModuleWorkflow} from "./modules/workflow/module.workflow";
import {createRedisConnection} from "./modules/caching/redis.utils";
import {BullModule} from "@nestjs/bullmq";
import ModuleDatabase from "./modules/database/module.database";
import {OnesignalModule} from "./modules/onesignal/onesignal.module";
import {RedisModule} from "./modules/caching/io.redis.module";
import {EurekaModule} from "nestjs-eureka";
import {ClientKafkaModule} from "./modules/kafka/kafka.module";

@Module({
    imports: [...ModuleDatabase,
        RedisModule.forRoot(createRedisConnection({config: null, bullmq: false})),
        ModuleWorkflow,
        OnesignalModule,
        BullModule.forRoot({
            defaultJobOptions: {
                backoff: 5,
            },
            sharedConnection: true,
            connection: createRedisConnection({config: null, bullmq: true}),
            blockingConnection: true,
        }),
        EurekaModule.forRoot({
            eureka: {
                host: process.env.EUREKA_HOST,
                port: process.env.EUREKA_PORT,
                registryFetchInterval: 60 * 1000,
                servicePath: '/eureka/apps',
                maxRetries: 3,
            },
            service: {
                name: process.env.APPLICATION_NAME,
                port: Number(process.env.PORT || 90),
            },
        }),
        ClientKafkaModule
    ],
    controllers: [AppController],
    providers: [AppService],
})
export class AppModule {
}
