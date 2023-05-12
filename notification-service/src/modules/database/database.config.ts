import type { IDatabaseConfig } from './interfaces/dbConfig.interface';

/**
 *  npx sequelize-cli db:migrate requires a module export and cant
 *  use type IDatabaseConfig.  Load from contig.ts and wrap as IDatabaseConfig
 */

import { default as config } from './config';

//dotenv.config();

export const databaseConfig: IDatabaseConfig = {
  development: config.development,
  test: config.test,
  production: config.production,
};
