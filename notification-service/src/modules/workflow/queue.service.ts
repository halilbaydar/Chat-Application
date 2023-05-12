import {Injectable} from "@nestjs/common";
import {InjectQueue, QueueEventsHost} from "@nestjs/bullmq";
import {QueueName} from "./core/queue.name";
import {Job, JobsOptions, Queue} from "bullmq";
import TraceQueueConfig from "./core/job.config";
import {WorkerImportance} from "./worker/worker.importance";
import {JobData} from "./core/job.data";
import {INotification} from "./notification.consumer/model";
import * as OneSignal from "@onesignal/node-onesignal";

@Injectable()
export class QueueService extends QueueEventsHost {
    constructor(@InjectQueue(QueueName.USER_NOTIFICATION_SETTINGS) private readonly queueUserNotificationSettings: Queue,
                @InjectQueue(QueueName.INSTANT_NOTIFICATION_SENDER) private readonly queueUserNotificationSender: Queue) {
        super();
    }

    public getQueueByName(name: QueueName): Queue {
        switch (name) {
            case QueueName.INSTANT_NOTIFICATION_SENDER:
                return this.queueUserNotificationSender;
            case QueueName.USER_NOTIFICATION_SETTINGS:
                return this.queueUserNotificationSettings
        }
    }

    @TraceQueueConfig(WorkerImportance.LOW)
    async addQueueUserNotificationSettings(data: JobData<INotification>, options?: JobsOptions): Promise<Job<JobData<INotification>>> {
        return await this.queueUserNotificationSettings.add(QueueName.USER_NOTIFICATION_SETTINGS, data, {...this.queueUserNotificationSettings.jobsOpts, ...options});
    }

    @TraceQueueConfig(WorkerImportance.LOW)
    async addQueueUserNotificationSender(data: JobData<OneSignal.Notification>, options?: JobsOptions): Promise<Job<JobData<INotification>>> {
        return await this.queueUserNotificationSender.add(QueueName.INSTANT_NOTIFICATION_SENDER, data, {...this.queueUserNotificationSettings.jobsOpts, ...options});
    }
}