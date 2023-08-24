import {DynamicModule, Module} from "@nestjs/common";
import {OneSignalService} from "./one.signal.ervice";
import {IOneSignalModuleOptions} from "./interfaces";

export const ONESIGNAL_MODULE_OPTIONS =  {
    appId: process.env.NOTIFICATION_APP_ID,
    restApiKey: process.env.NOTIFICATION_API_KEY
}

@Module({
    providers: [OneSignalService],
    exports: [OneSignalService]
})
export class OnesignalModule {
}