import type {RedisOptions} from 'ioredis';
import * as dotenv from 'dotenv';

dotenv.config();

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

const WORKFLOW_REDIS = getBullRedisPermConfig()