import {QueueName} from "../core/queue.name";
import {QueueEventsOptions} from "bullmq";

export interface QueueEventConfig extends QueueEventsOptions {
    name: QueueName;
}

export const EventConfig: QueueEventsOptions = {
    autorun: true,
    sharedConnection: true,
    prefix: 'ntf:bull',
};