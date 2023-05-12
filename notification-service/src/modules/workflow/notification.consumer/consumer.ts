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
import {Inject} from "@nestjs/common";
import {
    NOTIFICATION_SETTINGS_REPOSITORY,
    NotificationSettingsEntity
} from "../../notification/notification.settings.entity";
import {NotificationReason, NotificationType} from "../../notification";
import {NOTIFICATION_REPOSITORY, NotificationEntity} from "../../notification/notification.entity";
import {NotificationSenderService} from "../../onesignal/notification.sender";

@Worker({name: QueueName.USER_NOTIFICATION_SETTINGS, importance: WorkerImportance.CRITICAL})
export class UserNotificationSettingsConsumer extends ParentConsumer {
    constructor(
        @InjectRedis() private readonly redisClient: RedisClient,
        @Inject(NOTIFICATION_SETTINGS_REPOSITORY) private readonly notificationSettingsRepository: typeof NotificationSettingsEntity,
        @Inject(NOTIFICATION_REPOSITORY) private readonly notificationRepository: typeof NotificationEntity,
        private readonly notificationSender: NotificationSenderService
    ) {
        super();
    }

    async process(job: Job<JobData<INotification>>, token: string | undefined): Promise<any> {
        const {userId, notificationId, reason, type} = job.data.data
        const userString = await this.redisClient.hget("users", userId)
        const user: UserCacheModel = JSON.parse(userString)

        const notification = await this.notificationRepository.findOne({where: {userId, type}})
        const settings = await this.notificationSettingsRepository.findOne({where: {userId: user.id}})

        switch (type) {
            case NotificationType.MOBILE:
                switch (reason) {
                    case NotificationReason.DIRECT_MESSAGE: {
                        if (!settings.settings.dontDisturbMe) {
                            if (settings.settings.check.instantNotification.mobileNotification.enabled) {
                                switch (notification.provider) {
                                    case 'onesignal': {
                                        await this.notificationSender.send("")//TODO implement here
                                    }
                                }
                            }
                        }
                    }
                    case NotificationReason.MENTION:
                }
            case NotificationType.DESKTOP:
                switch (reason) {
                    case NotificationReason.DIRECT_MESSAGE:
                    case NotificationReason.MENTION:
                }
            case NotificationType.SMS:
                switch (reason) {
                    case NotificationReason.DIRECT_MESSAGE:
                    case NotificationReason.MENTION:
                }
            case NotificationType.WEB:
                switch (reason) {
                    case NotificationReason.DIRECT_MESSAGE:
                    case NotificationReason.MENTION:
                }
            case NotificationType.MAIL:
                switch (reason) {
                    case NotificationReason.DIRECT_MESSAGE:
                    case NotificationReason.MENTION:
                }
        }
        return Promise.resolve(undefined);
    }
}