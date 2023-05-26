import {Module} from '@nestjs/common';
import {AppController} from './app.controller';
import {AppService} from './app.service';
import {ModuleWorkflow} from "./modules/workflow/module.workflow";
import {createRedisConnection} from "./modules/caching/redis.utils";
import {BullModule} from "@nestjs/bullmq";
import ModuleDatabase from "./modules/database/module.database";
import {NotificationModule} from "./modules/notification/notification.module";
import {OnesignalModule} from "./modules/onesignal/onesignal.module";
import {RedisModule} from "./modules/caching/io.redis.module";
import {EurekaModule} from "nestjs-eureka";

@Module({
    imports: [...ModuleDatabase,
        OnesignalModule.register({
            appId: process.env.NOTIFICATION_APP_ID,
            restApiKey: process.env.NOTIFICATION_API_KEY
        }),
        RedisModule.forRoot(createRedisConnection({config: null, bullmq: false})),
        NotificationModule, ModuleWorkflow, BullModule.forRoot({
            defaultJobOptions: {
                backoff: 5,
            },
            sharedConnection: true,
            connection: createRedisConnection({config: null, bullmq: true}),
            blockingConnection: true,
        }),
        EurekaModule.forRoot({
            eureka: {
                host: 'localhost',
                port: 80,
                registryFetchInterval:  60 * 1000,
                servicePath: '/eureka/apps',
                maxRetries: 3,
            },
            service: {
                name: process.env.APPLICATION_NAME,
                port: Number(process.env.PORT || 90),
            },
        }),
    ],
    controllers: [AppController],
    providers: [AppService],
})
export class AppModule {
}
