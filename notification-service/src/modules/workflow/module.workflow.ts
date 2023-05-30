import {Module} from '@nestjs/common';
import {NotificationConsumer} from "../kafka/notification-consumer/notification.consumer";
import {NotificationSenderConsumer, NotificationSenderListener} from "./onesignal.consumer/consumer";
import {BullModule} from "@nestjs/bullmq";
import {WORKFLOW_CONFIG} from "./core/queue.configs";

@Module({
    imports: [BullModule.registerQueue(...WORKFLOW_CONFIG)],
    controllers: [],
    providers: [
        NotificationConsumer,
        NotificationSenderConsumer,
        NotificationSenderListener
    ]
})
export class ModuleWorkflow {

}