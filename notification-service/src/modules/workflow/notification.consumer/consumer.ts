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
import {QueueService} from "../queue.service";
import * as moment from "moment-timezone";

@Worker({name: QueueName.USER_NOTIFICATION_SETTINGS, importance: WorkerImportance.CRITICAL})
export class UserNotificationSettingsConsumer extends ParentConsumer {
    constructor(
        @InjectRedis() private readonly redisClient: RedisClient,
        @Inject(NOTIFICATION_SETTINGS_REPOSITORY) private readonly notificationSettingsRepository: typeof NotificationSettingsEntity,
        @Inject(NOTIFICATION_REPOSITORY) private readonly notificationRepository: typeof NotificationEntity,
        private readonly queueManagerService: QueueService
    ) {
        super();
    }

    async process(job: Job<JobData<INotification>>, token: string | undefined): Promise<any> {
        const {userId, reason, type, title, subTitle, message, traceId, groupName} = job.data.data
        const userString = await this.redisClient.hget("users", userId)
        const user: UserCacheModel = JSON.parse(userString)

        const notification = await this.notificationRepository.findOne({where: {userId, type}})
        const settings = await this.notificationSettingsRepository.findOne({where: {userId: user.id}})

        if (!this.dontDisturbMe(settings.settings.dontDisturbMe, user.timezone)) {
            switch (type) {
                case NotificationType.MOBILE: {
                    await this.handleMobileNotification(settings, reason, notification, user, title, subTitle, message, groupName);
                }
                    break
                case NotificationType.DESKTOP:
                    switch (reason) {
                        case NotificationReason.DIRECT_MESSAGE:
                        case NotificationReason.MENTION:
                        case NotificationReason.GROUP:
                    }
                    break
                case NotificationType.SMS:
                    switch (reason) {
                        case NotificationReason.DIRECT_MESSAGE:
                        case NotificationReason.MENTION:
                        case NotificationReason.GROUP:
                    }
                    break
                case NotificationType.WEB:
                    switch (reason) {
                        case NotificationReason.DIRECT_MESSAGE:
                        case NotificationReason.MENTION:
                        case NotificationReason.GROUP:
                    }
                    break
                case NotificationType.MAIL :
                    switch (reason) {
                        case NotificationReason.DIRECT_MESSAGE:
                        case NotificationReason.MENTION:
                        case NotificationReason.GROUP:
                    }
                    break
            }
        }
        //TODO complete other parts
        return Promise.resolve(undefined);
    }

    private async handleMobileNotification(settings: NotificationSettingsEntity, reason: NotificationReason, notification: NotificationEntity, user: UserCacheModel, title: string, subTitle: string, message: string, groupName: string) {
        if (settings.settings.check.instantNotification.mobileNotification.enabled) {
            switch (reason) {
                case NotificationReason.DIRECT_MESSAGE: {
                    switch (notification.provider) {
                        case 'onesignal': {
                            await this.doSend(user, title, subTitle, notification, message);
                        }
                            break
                    }
                }
                    break
                case NotificationReason.MENTION: {
                    if (settings.settings.mentionNotification.enabled) {
                        switch (notification.provider) {
                            case 'onesignal': {
                                await this.doSend(user, title, subTitle, notification, message);
                            }
                                break
                        }
                    }
                }
                    break
                case NotificationReason.GROUP: {
                    if (settings.settings.groupNotifications.enabled) {
                        const group = settings.settings.groupNotifications
                            .groups.find(({name}) => name === groupName)
                        if (!group?.muted) {
                            switch (notification.provider) {
                                case 'onesignal': {
                                    await this.doSend(user, title, subTitle, notification, message);
                                }
                                    break
                            }
                        }
                    }
                }
            }
        }
    }

    private async doSend(user: UserCacheModel, title: string, subTitle: string, notification: NotificationEntity, message: string) {
        await this.queueManagerService.addQueueUserNotificationSender({
            data: {
                headings: {
                    [user.language]: title,
                },
                subtitle: {
                    [user.language]: subTitle
                },
                include_player_ids: [notification.externalNotificationId],
                android_channel_id: process.env.ANDROID_CHANNEL_ID,
                contents: {
                    [user.language]: message
                },
            },
            traceId: ""
        })
    }

    private dontDisturbMe(settings: [{
        startDay: number;
        endDay: number;
        startTime: number;
        endTime: number
    }], timezone: string): boolean {
        const mom = moment.tz(timezone)
        const timeUnix = mom.startOf("day").unix()
        const weekday = mom.weekday()

        return settings.some(setting => {
            const {startDay, endDay, startTime, endTime} = setting
            if (startDay <= weekday && weekday <= endDay) {
                if (startTime <= timeUnix && timeUnix <= endTime) {
                    return true;
                }
            }
            return false
        })
    }
}