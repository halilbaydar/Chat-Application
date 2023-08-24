import {ONESIGNAL_MODULE_OPTIONS} from "./onesignal.module";
import {forwardRef, Inject, Injectable} from "@nestjs/common";
import {OneSignalAppClient} from "onesignal-api-client-core";
import {IOneSignalModuleOptions} from "./interfaces";

@Injectable()
export class OneSignalService extends OneSignalAppClient {
    constructor() {
        super(ONESIGNAL_MODULE_OPTIONS.appId, ONESIGNAL_MODULE_OPTIONS.restApiKey);
    }
}