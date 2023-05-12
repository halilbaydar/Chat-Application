export interface NotificationSettings {
    check: {
        instantNotification: {
            mobileNotification: {
                enabled: boolean
            },
            webNotification: {
                enabled: boolean
            },
            desktopNotification: {
                enabled: boolean
            },
            sound: boolean
        }
        EMAIL_NOTIFICATION_ENABLED: boolean
        SMS_NOTIFICATION_ENABLED: boolean
        CHAT_NOTIFICATION: boolean
    },
    dontDisturbMe: {
        startDay: number,
        endDay: number
        startTime: number,
        endTime: number,
    },
    mentionNotification: {
        enabled: boolean
    },
    groupNotifications: {
        enabled: boolean,
        groups: [{
            name: string
        }]
    }
}

export enum NotificationResult {
    SUCCESS,
    ERROR,
}