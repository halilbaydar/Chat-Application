import {Injectable} from "@nestjs/common";
import {INotification} from "onesignal-api-client-core/lib/dto/notifications";

@Injectable()
export class NotificationSenderService {
    constructor() {
    }

    public async send(data: any): Promise<string> {
        try {
            const notification: INotification = {
                included_segments: ['Subscribed Users'],
                contents: {
                    en: "Hello OneSignal!"
                },
            }
            return ""
        } catch (err) {

        }
    }
}