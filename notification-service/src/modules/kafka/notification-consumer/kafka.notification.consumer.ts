import {Controller, Inject, OnModuleInit} from "@nestjs/common";
import {ClientKafka, Ctx, EventPattern, KafkaContext, NatsContext, Payload, Transport} from "@nestjs/microservices";
import {IChatMessageNotification} from "../model/model";
import {Consumer} from "../core/consumer";

export interface UserKafkaModel {
    id: bigint;
    createdAt: bigint;
    name: string;
    username: string;
    role: string
}

@Controller()
export class KafkaNotificationConsumer implements Consumer<IChatMessageNotification, NatsContext>, OnModuleInit {
    constructor(@Inject("NOTIFICATION_SERVICE_KAFKA_CLIENT") private readonly client: ClientKafka) {
    }

    async onModuleInit() {
    }

    @EventPattern('notification.chat.message.receive')
    public async consume(payload: IChatMessageNotification, @Ctx() context: NatsContext): Promise<void> {
        const headers = context.getHeaders()
        const traceId = headers["traceId"]
    }

    @EventPattern('USER_REGISTER_KAFKA_TOPIC', Transport.KAFKA)
    public async consumeRegisteredUser(@Payload() message: UserKafkaModel, @Ctx() context: KafkaContext): Promise<void> {
        const originalMessage = context.getMessage();
        const partition = context.getPartition();
        const {headers, timestamp} = originalMessage;
        console.log({message, headers, timestamp})
    }
}