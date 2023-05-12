import {Module} from "@nestjs/common";
import {NotificationProvider} from "./entity.provider";


@Module({
    providers: [...NotificationProvider]
})
export class NotificationModule {
}