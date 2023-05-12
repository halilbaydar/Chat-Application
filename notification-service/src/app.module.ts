import {Module} from '@nestjs/common';
import {AppController} from './app.controller';
import {AppService} from './app.service';
import {ModuleWorkflow} from "./modules/workflow/module.workflow";
import {createRedisConnection} from "./modules/caching/redis.utils";
import {BullModule} from "@nestjs/bullmq";

@Module({
    imports: [ModuleWorkflow, BullModule.forRoot({
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
