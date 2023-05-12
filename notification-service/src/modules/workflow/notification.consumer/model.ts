import {NotificationReason, NotificationType} from "../../notification";

export interface INotification {
    userId: string,
    message: string,
    title: string,
    subTitle: string,
    type: NotificationType,
    reason: NotificationReason
    traceId: string,
    groupName?: string
}