import {NotificationReason, NotificationType} from "../../notification";

export interface INotification {
    userId: string,
    notificationId: string,
    message: string,
    type: NotificationType,
    reason: NotificationReason
}