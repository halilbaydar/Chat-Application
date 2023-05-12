import type { ModuleMetadata, Type } from '@nestjs/common/interfaces';
import type { RedisOptions } from 'ioredis';

export interface RedisModuleOptions {
  config: RedisOptions & { url?: string };
  bullmq?: boolean;
}

export interface RedisModuleOptionsFactory {
  createRedisModuleOptions(): Promise<RedisModuleOptions> | RedisModuleOptions;
}

export interface RedisModuleAsyncOptions extends Pick<ModuleMetadata, 'imports'> {
  inject?: any[];
  useClass?: Type<RedisModuleOptionsFactory>;
  useExisting?: Type<RedisModuleOptionsFactory>;
  useFactory?: (...args: any[]) => Promise<RedisModuleOptions> | RedisModuleOptions;
}
