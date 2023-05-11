import {WorkerImportance} from "../worker/worker.importance";
import type {JobsOptions} from 'bullmq';

export default function TraceQueueConfig(config: WorkerImportance): MethodDecorator {
    return function decorator(target: any, _propertyKey: string, descriptor: PropertyDescriptor): void {
        const method = descriptor.value;
        descriptor.value = async function wrapper(this, ...args: any[]) {
            const data = args[0];
            let options = args[1];
            options = {...createJobOptions(config), ...options};
            return await method.apply(this, [data, options]);
        };
    };
}

function createJobOptions(option: WorkerImportance): JobsOptions {
    switch (option) {
        case WorkerImportance.LOW:
            return {
                attempts: 1,
                delay: 0,
                removeOnComplete: true,
                removeOnFail: true,
            };
        case WorkerImportance.LOW_MEDIUM:
            return {
                attempts: 5,
                delay: 0,
                backoff: {
                    type: 'custom',
                },
                removeOnComplete: {age: 3600 * 3, count: 5},
                removeOnFail: {age: 3600 * 3, count: 15},
            };
        case WorkerImportance.MEDIUM:
            return {
                attempts: 10,
                delay: 0,
                backoff: {
                    type: 'custom',
                },
                removeOnComplete: {age: 3600 * 5, count: 10},
                removeOnFail: {age: 3600 * 5, count: 25},
            };
        case WorkerImportance.MEDIUM_HIGH:
            return {
                attempts: 15,
                delay: 0,
                backoff: {
                    type: 'custom',
                },
                removeOnComplete: {age: 3600 * 10, count: 30},
                removeOnFail: {age: 3600 * 10, count: 60},
            };
        case WorkerImportance.HIGH:
            return {
                attempts: 20,
                delay: 0,
                backoff: {
                    type: 'custom',
                },
                removeOnComplete: {age: 3600 * 15, count: 40},
                removeOnFail: {age: 3600 * 15, count: 80},
            };
        case WorkerImportance.CRITICAL:
            return {
                attempts: 20,
                delay: 0,
                backoff: {
                    type: 'custom',
                },
                removeOnComplete: {age: 3600 * 24, count: 1000},
                removeOnFail: {age: 3600 * 24, count: 1000},
            };
        case WorkerImportance.DEFAULT:
            return {
                attempts: 5,
                delay: 0,
                backoff: {
                    type: 'custom',
                },
                removeOnComplete: {age: 3600 * 24, count: 100},
                removeOnFail: {age: 3600 * 24, count: 1000},
            };
        default:
            return {};
    }
}