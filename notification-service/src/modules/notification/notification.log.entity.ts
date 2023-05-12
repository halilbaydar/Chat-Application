import {ParentEntity} from "../database/parent.entity";
import {BelongsTo, Column, DataType, ForeignKey, Table} from "sequelize-typescript";
import {NotificationResult} from "./index";
import {NotificationEntity} from "./notification.entity";

export const NOTIFICATION_LOG_REPOSITORY = 'NOTIFICATION_LOG_REPOSITORY'

@Table({
    tableName: "notification_log",
    underscored: true,
    paranoid: true,
})
export class NotificationLogEntity extends ParentEntity<NotificationLogEntity> {

    @Column({
        type: DataType.BIGINT
    })
    userId: string

    @Column({
        type: DataType.STRING
    })
    @ForeignKey(() => NotificationEntity)
    notificationId: string

    @BelongsTo(() => NotificationEntity)
    notification: NotificationEntity

    @Column({
        type: DataType.JSON
    })
    data: object

    @Column({
        type: DataType.STRING
    })
    result: NotificationResult
}