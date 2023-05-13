import {Controller} from "@nestjs/common";
import {Ctx, EventPattern, NatsContext, Payload} from "@nestjs/microservices";
import {IChatMessageNotification} from "../model/model";

@Controller()
export class NotificationController {

    @EventPattern('notification.chat.message.receive')
    public async killDragon(@Payload() message: IChatMessageNotification, @Ctx() context: NatsContext): Promise<void> {
        const headers = context.getHeaders()
        const traceId = headers["traceId"]
    }
}