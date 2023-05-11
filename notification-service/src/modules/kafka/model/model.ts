export enum NotificationType {
    MAIL, INSTANT, SMS
}

export enum NotificationPriority {
    LOW,
    NORMAL,
    HIGH
}

export interface NotificationDto<T> {
    data: T
    userId: string
    notificationId: string
    timestamp: number
    type: NotificationType,
    priority: NotificationPriority,
}

export interface IChatMessageNotification extends NotificationDto<string> {
    sound: string,
}