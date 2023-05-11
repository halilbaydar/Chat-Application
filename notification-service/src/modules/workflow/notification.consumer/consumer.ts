import {ParentConsumer} from "../core/parent.consumer";
import {Job} from "bullmq";
import {Worker} from "../worker/worker";
import {QueueName} from "../core/queue.name";
import {WorkerImportance} from "../worker/worker.importance";
import {JobData} from "../core/job.data";
import {INotification} from "./model";


@Worker({name: QueueName.USER_NOTIFICATION_SETTINGS, importance: WorkerImportance.CRITICAL})
export class UserNotificationSettingsConsumer extends ParentConsumer {
    constructor() {
        super();
    }

    process(job: Job<JobData<INotification>>, token: string | undefined): Promise<any> {
        return Promise.resolve(undefined);
    }
}