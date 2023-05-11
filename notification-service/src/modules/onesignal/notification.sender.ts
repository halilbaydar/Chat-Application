import {Inject, Injectable} from "@nestjs/common";
import {ONE_SIGNAL} from "./onesignal.module";
import {PromiseDefaultApi} from "@onesignal/node-onesignal/types/PromiseAPI";
import * as OneSignal from '@onesignal/node-onesignal';
import * as process from "process";

@Injectable()
export class NotificationSenderService {
    constructor(@Inject(ONE_SIGNAL) private readonly oneSignalClient: PromiseDefaultApi) {
    }

    public async send(data: any): Promise<string> {
        try {
            const notification = new OneSignal.Notification();
            notification.app_id = process.env.ONESIGNAL_APP_ID;
            notification.included_segments = ['Subscribed Users'];
            notification.contents = {
                en: "Hello OneSignal!"
            };
            const {id} = await this.oneSignalClient.createNotification(notification);
            return id
        } catch (err) {

        }
    }
}