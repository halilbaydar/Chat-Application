import type {IDatabaseConfig} from './interfaces/dbConfig.interface';

import {default as config} from './config';

export const databaseConfig: IDatabaseConfig = {
    development: config.development,
    test: config.test,
    production: config.production,
};
