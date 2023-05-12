import type { DynamicModule } from '@nestjs/common';
import { Module } from '@nestjs/common';
import { RedisCoreModule } from './redis.core-module';
import type { RedisModuleAsyncOptions, RedisModuleOptions } from './redis.interfaces';

@Module({})
export class RedisModule {
  public static forRoot(options?: RedisModuleOptions, connection?: string): DynamicModule {
    return {
      module: RedisModule,
      imports: [RedisCoreModule.forRoot(options, connection)],
      exports: [RedisCoreModule],
    };
  }

  public static forRootAsync(options: RedisModuleAsyncOptions, connection?: string): DynamicModule {
    return {
      module: RedisModule,
      imports: [RedisCoreModule.forRootAsync(options, connection)],
      exports: [RedisCoreModule],
    };
  }
}
