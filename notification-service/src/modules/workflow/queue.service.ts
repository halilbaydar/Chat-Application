import {Injectable} from "@nestjs/common";
import {InjectQueue, QueueEventsHost} from "@nestjs/bullmq";
import {QueueName} from "./core/queue.name";
import {Job, JobsOptions, Queue} from "bullmq";
import TraceQueueConfig from "./core/job.config";
import {WorkerImportance} from "./worker/worker.importance";
import {JobData} from "./core/job.data";
import {INotification} from "./notification.consumer/model";

@Injectable()
export class QueueService extends QueueEventsHost {
    constructor(@InjectQueue(QueueName.USER_NOTIFICATION_SETTINGS) private readonly queueUserNotificationSettings: Queue) {
        super();
    }

    @TraceQueueConfig(WorkerImportance.LOW)
    async addQueueUserNotificationSettings(data: JobData<INotification>, options?: JobsOptions): Promise<Job<JobData<INotification>>> {
        return await this.queueUserNotificationSettings.add(QueueName.USER_NOTIFICATION_SETTINGS, data, {...this.queueUserNotificationSettings.jobsOpts, ...options});
    }
}