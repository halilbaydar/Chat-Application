import {Controller} from "@nestjs/common";
import {Ctx, EventPattern, NatsContext, Payload} from "@nestjs/microservices";
import {IChatMessageNotification} from "../model/model";
import {Consumer} from "../core/consumer";

@Controller()
export class NotificationConsumer implements Consumer<IChatMessageNotification,NatsContext>{
    @EventPattern('notification.chat.message.receive')
    public async consume(payload: IChatMessageNotification, @Ctx() context: NatsContext): Promise<void> {
        const headers = context.getHeaders()
        const traceId = headers["traceId"]
    }
}