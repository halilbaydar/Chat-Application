import {ParentEntity} from "../database/parent.entity";
import {BelongsTo, Column, DataType, ForeignKey, HasOne, Table} from "sequelize-typescript";
import {NotificationEntity} from "./notification.entity";
import {NotificationSettings} from "./index";

export const NOTIFICATION_SETTINGS_REPOSITORY = 'NOTIFICATION_SETTINGS_REPOSITORY'

@Table({
    tableName: "notification_settings",
    underscored: true,
    paranoid: true,
})
export class NotificationSettingsEntity extends ParentEntity<NotificationSettingsEntity> {

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
    settings: NotificationSettings
}