import {QueueEventsListener} from "@nestjs/bullmq";
import {applyDecorators} from "@nestjs/common";
import {EventConfig, QueueEventConfig} from "./config";

export function QueueEvent(config: QueueEventConfig): ClassDecorator {
    const {name} = config;
    const updatedEventConfig = {...EventConfig, ...config};
    return applyDecorators(QueueEventsListener(name, updatedEventConfig));
}