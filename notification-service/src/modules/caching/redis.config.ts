import * as dotenv from 'dotenv';
import type { RedisOptions } from 'ioredis';

dotenv.config();

export default [getRedisConfig()];

function getRedisConfig(): RedisOptions {
  const plain = process.env.REDIS_PLAIN === 'true';
  const db = process.env.REDIS_DB_NUMBER !== undefined ? Number(process.env.REDIS_DB_NUMBER) : undefined;
  if (plain) {
    return {
      name: 'test2',
      host: process.env.REDIS_HOST,
      port: Number(process.env.REDIS_PORT || '6379'),
      db,
    };
  }
  return {
    name: 'test2',
    host: process.env.REDIS_HOST,
    port: Number(process.env.REDIS_PORT || '6379'),
    username: process.env.REDIS_USER,
    password: process.env.REDIS_PASS,
    db,
    tls: {
      servername: process.env.REDIS_SERVERNAME || process.env.REDIS_HOST,
    },
  };
}

export function getBullRedisPermConfig(): RedisOptions {
  const plain = process.env.REDIS_PLAIN === 'true';
  const db = process.env.REDIS_BULL_DB_NUMBER !== undefined ? Number(process.env.REDIS_BULL_DB_NUMBER) : undefined;
  if (plain) {
    return {
      host: process.env.REDIS_HOST,
      port: Number(process.env.REDIS_PORT || '6379'),
      db,
    };
  }
  return {
    host: process.env.REDIS_HOST,
    port: Number(process.env.REDIS_PORT || '6379'),
    username: process.env.REDIS_USER,
    password: process.env.REDIS_PASS,
    db,
    tls:
      process.env.REDIS_PLAIN === 'true'
        ? undefined
        : {
            servername: process.env.REDIS_SERVERNAME || process.env.REDIS_HOST,
          },
  };
}
