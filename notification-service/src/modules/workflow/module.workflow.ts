import {Module} from '@nestjs/common';
import {NotificationSenderConsumer, NotificationSenderListener} from "./onesignal.consumer/consumer";
import {BullModule} from "@nestjs/bullmq";
import {WORKFLOW_CONFIG} from "./core/queue.configs";
import {NotificationSenderService} from "../onesignal/notification.sender";
import {QueueService} from "./queue.service";

@Module({
    imports: [BullModule.registerQueue(...WORKFLOW_CONFIG)],
    controllers: [],
    providers: [
        NotificationSenderConsumer,
        NotificationSenderListener,
        NotificationSenderService,
        QueueService
    ]
})
export class ModuleWorkflow {

}