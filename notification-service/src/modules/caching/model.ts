export enum UserStatus {
    ACTIVE, INACTIVE, DELETED, BANNED
}

export interface UserCacheModel {
    id: number
    username: string,
    notificationId: string,
    status: UserStatus
    language: string,
    timezone: string
}