import {Module} from '@nestjs/common';
import {AppController} from './app.controller';
import {AppService} from './app.service';
import {ModuleWorkflow} from "./modules/workflow/module.workflow";
import {createRedisConnection} from "./modules/caching/redis.utils";
import {BullModule} from "@nestjs/bullmq";
import ModuleDatabase from "./modules/database/module.database";
import {NotificationModule} from "./modules/notification/notification.module";
import {OnesignalModule} from "./modules/onesignal/onesignal.module";

@Module({
    imports: [...ModuleDatabase,
        OnesignalModule.register(),
        NotificationModule, ModuleWorkflow, BullModule.forRoot({
            defaultJobOptions: {
                backoff: 5,
            },
            sharedConnection: true,
            connection: createRedisConnection({config: null, bullmq: true}),
            blockingConnection: true,
        })],
    controllers: [AppController],
    providers: [AppService],
})
export class AppModule {
}
