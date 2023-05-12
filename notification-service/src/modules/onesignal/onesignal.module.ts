import {DynamicModule, Module} from "@nestjs/common";
import {NotificationSenderService} from "./notification.sender";
import * as OneSignal from '@onesignal/node-onesignal';
import {PromiseDefaultApi} from "@onesignal/node-onesignal/types/PromiseAPI";

export const NOTIFICATION_SENDER_TOKEN = "ONE_SIGNAL";

@Module({})
export class OnesignalModule {
    public static register(): DynamicModule {
        return {
            module: OnesignalModule,
            providers: [NotificationSenderService, {
                provide: NOTIFICATION_SENDER_TOKEN,
                useFactory: () => {
                    const configuration = OneSignal.createConfiguration({
                        authMethods: {
                            app_key: {
                                tokenProvider: {
                                    getToken(): Promise<string> | string {
                                        return process.env.INSTANT_NOTIFICATON_SENDER_TOKEN
                                    }
                                }
                            }
                        }
                    });
                    return new OneSignal.DefaultApi(configuration)
                }
            }],
            exports: [NotificationSenderService, PromiseDefaultApi]
        }
    }
}