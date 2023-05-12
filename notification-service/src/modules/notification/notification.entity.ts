import {ParentEntity} from "../database/parent.entity";
import {Column, DataType, Table} from "sequelize-typescript";
import {NotificationType} from "./index";

export const NOTIFICATION_REPOSITORY = 'NOTIFICATION_REPOSITORY'

@Table({
    tableName: "notifications",
    underscored: true,
    paranoid: true,
})
export class NotificationEntity extends ParentEntity<NotificationEntity> {

    @Column({
        type: DataType.BIGINT
    })
    userId: string

    @Column({
        type: DataType.STRING
    })
    externalNotificationId: string

    @Column({
        type: DataType.STRING
    })
    provider: string
,

    @Column({
        type: DataType.STRING
    })
    type: NotificationType
}