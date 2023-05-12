import {ParentConsumer} from "../core/parent.consumer";
import {Job} from "bullmq";
import {Worker} from "../worker/worker";
import {QueueName} from "../core/queue.name";
import {WorkerImportance} from "../worker/worker.importance";
import {JobData} from "../core/job.data";
import {INotification} from "./model";
import {RedisClient} from "../../caching/redis.utils";
import {InjectRedis} from "../../caching/redis.decorators";
import {UserCacheModel} from "../../caching/model";

@Worker({name: QueueName.USER_NOTIFICATION_SETTINGS, importance: WorkerImportance.CRITICAL})
export class UserNotificationSettingsConsumer extends ParentConsumer {
    constructor(
        @InjectRedis() private readonly redisClient: RedisClient,
    ) {
        super();
    }

    async process(job: Job<JobData<INotification>>, token: string | undefined): Promise<any> {
        const {userId, notificationId} = job.data.data
        const userString = await this.redisClient.hget("users", userId)
        const user: UserCacheModel = JSON.parse(userString)

        return Promise.resolve(undefined);
    }
}