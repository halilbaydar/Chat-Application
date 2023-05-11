import {RegisterQueueOptions} from "@nestjs/bullmq";
import {QueueName} from "../core/queue.name";

export const config: RegisterQueueOptions[] = [
    {
        name: QueueName.USER_NOTIFICATION_SETTINGS,
        sharedConnection: true,
        prefix: 'ntf:bull',
        defaultJobOptions: {
            attempts: 1,
            removeOnComplete: true,
            removeOnFail: true,
        },
    }
]