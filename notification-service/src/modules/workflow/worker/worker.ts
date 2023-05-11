import type {WorkerOptions} from 'bullmq/dist/esm/interfaces/worker-options';
import {applyDecorators} from '@nestjs/common';
import {Processor} from '@nestjs/bullmq';
import {UnrecoverableError} from 'bullmq';
import {WorkerImportance} from "./worker.importance";

export interface WorkerOps extends WorkerOptions {
    name: string;
    importance: WorkerImportance;
}

export function Worker(ops: WorkerOps): ClassDecorator {
    const {name, importance}: WorkerOps = ops;
    const updatedConfig = {...createWorkerOptions(importance), ...ops};
    return applyDecorators(Processor(name, updatedConfig));
}

const backoffStrategyOpts = {
    settings: {
        backoffStrategy(attempts, type, err: any): number {
            switch (type) {
                case 'custom':
                    if (err?.status == 404) {
                        throw new UnrecoverableError(err?.message);
                    }
                    return 2 ^ ((attempts - 1) * 100);
                case 'example':
                    if (err?.status == 401) {
                        throw new UnrecoverableError(err?.message);
                    }
                    return 2 ^ ((attempts - 1) * 100);
                default:
                    if (err?.status == 404) {
                        throw new UnrecoverableError(err?.message);
                    }
                    return 2 ^ ((attempts - 1) * 100);
            }
        },
    },
};

function createWorkerOptions(importance: WorkerImportance): WorkerOptions {
    switch (importance) {
        case WorkerImportance.CRITICAL:
            return {
                concurrency: 3,
                sharedConnection: true,
                autorun: true,
                prefix: 'jsa:bull',
                limiter: {
                    max: 100,
                    duration: 100,
                },
                ...backoffStrategyOpts,
            };
        case WorkerImportance.DEFAULT:
            return {
                autorun: true,
                concurrency: 3,
                sharedConnection: true, //
                prefix: 'jsa:bull',
                limiter: {
                    max: 100,
                    duration: 100,
                },
                ...backoffStrategyOpts,
            };
        case WorkerImportance.HIGH:
            return {
                concurrency: 3,
                sharedConnection: true,
                autorun: true,
                prefix: 'jsa:bull',
                limiter: {
                    max: 100,
                    duration: 100,
                },
                ...backoffStrategyOpts,
            };
        case WorkerImportance.LOW:
            return {
                concurrency: 3,
                sharedConnection: true,
                autorun: true,
                prefix: 'jsa:bull',
                limiter: {
                    max: 100,
                    duration: 100,
                },
                ...backoffStrategyOpts,
            };
        case WorkerImportance.LOW_MEDIUM:
            return {
                concurrency: 3,
                sharedConnection: true,
                autorun: true,
                prefix: 'jsa:bull',
                limiter: {
                    max: 100,
                    duration: 100,
                },
                ...backoffStrategyOpts,
            };
        case WorkerImportance.MEDIUM:
            return {
                concurrency: 3,
                sharedConnection: true,
                autorun: true,
                prefix: 'jsa:bull',
                limiter: {
                    max: 100,
                    duration: 100,
                },
                ...backoffStrategyOpts,
            };
        case WorkerImportance.MEDIUM_HIGH:
            return {
                concurrency: 3,
                sharedConnection: true,
                autorun: true,
                prefix: 'jsa:bull',
                limiter: {
                    max: 100,
                    duration: 100,
                },
                ...backoffStrategyOpts,
            };
    }
}