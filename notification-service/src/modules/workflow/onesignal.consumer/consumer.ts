import {ParentConsumer} from "../core/parent.consumer";
import {Worker} from "../worker/worker";
import {QueueName} from "../core/queue.name";
import {WorkerImportance} from "../worker/worker.importance";
import {NotificationSenderService} from "../../onesignal/notification.sender";
import {JobData} from "./model";
import {Job} from "bullmq";
import {OnQueueEvent, QueueEventsHost} from "@nestjs/bullmq";
import {QueueEvent} from "../event/event.handler";
import {QueueService} from "../queue.service";
import {INotification} from "onesignal-api-client-core/lib/dto/notifications";

@Worker({name: QueueName.INSTANT_NOTIFICATION_SENDER, importance: WorkerImportance.DEFAULT})
export class NotificationSenderConsumer extends ParentConsumer {
    constructor(private readonly notificationSender: NotificationSenderService) {
        super();
    }

    async process(job: Job<JobData<INotification>>, token: string | undefined): Promise<any> {
        try {
            await this.notificationSender.send(job.data.data)
            return Promise.resolve(undefined);
        } catch (err) {
            //TODO add logger
        }
    }
}

@QueueEvent({name: QueueName.INSTANT_NOTIFICATION_SENDER})
export class NotificationSenderListener extends QueueEventsHost {
    constructor(private readonly queueService: QueueService) {
        super();
    }

    @OnQueueEvent('completed')
    async completed(args: {
        jobId: string;
        returnvalue: string;
        prev?: string;
    }, id: string): Promise<void> {
        const queue = this.queueService.getQueueByName(QueueName.INSTANT_NOTIFICATION_SENDER);
        await queue.remove(args.jobId);
    }

    @OnQueueEvent('failed')
    async onFailed(
        args: {
            jobId: string;
            failedReason: string;
            prev?: string;
        },
        id: string,
    ) {
        const queue = this.queueService.getQueueByName(QueueName.INSTANT_NOTIFICATION_SENDER);
        await queue.remove(args.jobId);
    }

    @OnQueueEvent('removed')
    onRemoved(
        args: {
            jobId: string;
            prev: string;
        },
        id: string,
    ) {

    }

}