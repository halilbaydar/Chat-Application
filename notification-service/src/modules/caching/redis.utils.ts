import type { RedisModuleOptions } from './redis.interfaces';
import IORedis from 'ioredis';
import { REDIS_MODULE_CONNECTION, REDIS_MODULE_CONNECTION_TOKEN, REDIS_MODULE_OPTIONS_TOKEN } from './redis.constants';
import redisConfig, { getBullRedisPermConfig } from './redis.config';

export class RedisClient extends IORedis {}

export const IOREDIS = new RedisClient({ ...redisConfig[0], maxRetriesPerRequest: null, enableReadyCheck: false });
export const IOREDIS_BULLMQ = new RedisClient({
  ...getBullRedisPermConfig(),
  maxRetriesPerRequest: null,
  enableReadyCheck: false,
});
export const IOREDIS_TOKEN = 'IOREDIS_TOKEN';

export function getRedisOptionsToken(connection: string = IOREDIS_TOKEN): string {
  return `${connection || REDIS_MODULE_CONNECTION}_${REDIS_MODULE_OPTIONS_TOKEN}`;
}

export function getRedisConnectionToken(connection: string = IOREDIS_TOKEN): string {
  return `${connection || REDIS_MODULE_CONNECTION}_${REDIS_MODULE_CONNECTION_TOKEN}`;
}

export function createRedisConnection(options: RedisModuleOptions = { config: null, bullmq: false }): any {
  const { config, bullmq } = options;
  if (config?.url) {
    return new IORedis(config.url, { ...config, maxRetriesPerRequest: null, enableReadyCheck: false });
  } else if (config) {
    return new IORedis({ ...config, maxRetriesPerRequest: null, enableReadyCheck: false });
  } else if (bullmq) {
    return IOREDIS_BULLMQ;
  } else {
    return IOREDIS;
  }
}
