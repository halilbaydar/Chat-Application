import {DynamicModule, Module} from "@nestjs/common";
import {OneSignalService} from "./one.signal.ervice";
import {IOneSignalModuleOptions} from "./interfaces";

export const ONESIGNAL_MODULE_OPTIONS = "ONESIGNAL_MODULE_OPTIONS";

@Module({
    providers: [OneSignalService],
    exports: [OneSignalService]
})
export class OnesignalModule {
    public static register(options: IOneSignalModuleOptions): DynamicModule {
        return {
            module: OnesignalModule,
            providers: this.createOneSignalProvider(options),
        };
    }

    private static createOneSignalProvider(options: IOneSignalModuleOptions) {
        return [
            {
                provide: ONESIGNAL_MODULE_OPTIONS,
                useValue: options || {},
            },
        ];
    }
}