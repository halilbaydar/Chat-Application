import {Injectable} from "@nestjs/common";
import {InjectQueue, QueueEventsHost} from "@nestjs/bullmq";
import {QueueName} from "./core/queue.name";
import {JobsOptions, Queue} from "bullmq";
import TraceQueueConfig from "./core/job.config";
import {WorkerImportance} from "./worker/worker.importance";

@Injectable()
export class QueueService extends QueueEventsHost {
    constructor(@InjectQueue(QueueName.USER_NOTIFICATION_SETTINGS) private readonly queueUserNotificationSettings: Queue) {
        super();
    }

    @TraceQueueConfig(WorkerImportance.LOW)
    async addLogCriteriaQueue<T>(data: T, options?: JobsOptions) {
        return await this.queueUserNotificationSettings.add(QueueName.USER_NOTIFICATION_SETTINGS, data, {...this.queueUserNotificationSettings.jobsOpts, ...options});
    }
}