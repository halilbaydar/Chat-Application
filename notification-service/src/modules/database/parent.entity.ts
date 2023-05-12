import {CreatedAt, DeletedAt, Model, UpdatedAt} from "sequelize-typescript";

export class ParentEntity<T> extends Model<T> {
    @CreatedAt
    createdAt: Date

    @DeletedAt
    deletedAt: Date

    @UpdatedAt
    updatedAt: Date
}