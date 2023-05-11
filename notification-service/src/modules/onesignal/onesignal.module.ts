import {DynamicModule, Module} from "@nestjs/common";
import {NotificationSenderService} from "./notification.sender";
import * as OneSignal from '@onesignal/node-onesignal';
import {TokenProvider} from '@onesignal/node-onesignal';
import {PromiseDefaultApi} from "@onesignal/node-onesignal/types/PromiseAPI";

export const ONE_SIGNAL = "ONE_SIGNAL";

@Module({})
export class OnesignalModule {
    public static register(token: TokenProvider): DynamicModule {
        return {
            module: OnesignalModule,
            providers: [NotificationSenderService, {
                provide: ONE_SIGNAL,
                useFactory: () => {
                    const configuration = OneSignal.createConfiguration({
                        authMethods: {
                            app_key: {
                                tokenProvider: token
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