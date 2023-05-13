import {Inject, Injectable} from "@nestjs/common";
import {ONESIGNAL_MODULE_OPTIONS} from "./onesignal.module";
import {OneSignalService} from "./one.signal.ervice";
import {INotification} from "onesignal-api-client-core/lib/dto/notifications";

@Injectable()
export class NotificationSenderService {
    constructor(private readonly oneSignalClient: OneSignalService) {
    }

    public async send(data: any): Promise<string> {
        try {
            const notification: INotification = {
                included_segments: ['Subscribed Users'],
                contents: {
                    en: "Hello OneSignal!"
                },
            }

            const {id} = await this.oneSignalClient.createNotification(notification);
            return id
        } catch (err) {

        }
    }
}