import {SequelizeModule} from '@nestjs/sequelize';
import {databaseConfig} from './database.config';
import {DEVELOPMENT, PRODUCTION, TEST} from './constants';
import {NotificationEntity} from "../notification/notification.entity";
import {NotificationSettingsEntity} from "../notification/notification.settings.entity";
import {DynamicModule} from "@nestjs/common";
import {NotificationLogEntity} from "../notification/notification.log.entity";

let default_config;
switch (process.env.NODE_ENV) {
    case DEVELOPMENT:
        default_config = databaseConfig.development.default;
        break;
    case TEST:
        default_config = databaseConfig.development.default;
        break;
    case PRODUCTION:
        default_config = databaseConfig.development.default;
        break;
    default:
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        default_config = databaseConfig.development.default;
}

const ModuleDatabase: DynamicModule[] = [
    SequelizeModule.forRootAsync({
        name: 'NOTIFICATION',
        useFactory: () => ({
            ...default_config,
            pool: {
                min: 5,
                max: 20,
                acquire: 6000,
                idle: 0,
            },
            logging: false,
            models: [NotificationEntity, NotificationSettingsEntity, NotificationLogEntity],
        }),
    }),
];

export default ModuleDatabase;
