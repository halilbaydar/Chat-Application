import {RegisterQueueOptions} from "@nestjs/bullmq";
import {NOTIFICATION_QUEUE_CONFIG} from "../notification.consumer/config";
import {ONE_SIGNAL_QUEUE_CONFIG} from "../onesignal.consumer/config";

export const WORKFLOW_CONFIG :RegisterQueueOptions[] = [...NOTIFICATION_QUEUE_CONFIG,...ONE_SIGNAL_QUEUE_CONFIG]