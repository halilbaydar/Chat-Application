import { Inject } from '@nestjs/common';
import { getRedisConnectionToken, IOREDIS_TOKEN } from './redis.utils';

export const InjectRedis = (connection: string = IOREDIS_TOKEN) => {
  return Inject(getRedisConnectionToken(connection));
};
