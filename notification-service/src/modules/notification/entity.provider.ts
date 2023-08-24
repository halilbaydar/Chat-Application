import {Provider} from "@nestjs/common";
import {NOTIFICATION_SETTINGS_REPOSITORY, NotificationSettingsEntity} from "./notification.settings.entity";
import {NOTIFICATION_REPOSITORY, NotificationEntity} from "./notification.entity";
import {NOTIFICATION_LOG_REPOSITORY, NotificationLogEntity} from "./notification.log.entity";

export const NotificationProvider: Provider[] = [{
    provide: NOTIFICATION_SETTINGS_REPOSITORY, useValue: NotificationSettingsEntity
}, {
    provide: NOTIFICATION_REPOSITORY, useValue: NotificationEntity
}, {
    provide: NOTIFICATION_LOG_REPOSITORY, useValue: NotificationLogEntity
}]