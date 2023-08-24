import {ClientsModule, Transport} from "@nestjs/microservices";
import {Module} from "@nestjs/common";
import {KafkaNotificationConsumer} from "./notification-consumer/kafka.notification.consumer";
import process from "node:process";

export const CLIENT_MODULE_NAME = "NOTIFICATION_SERVICE_KAFKA_CLIENT"

@Module({
    imports: [ClientsModule.register([
        {
            name: CLIENT_MODULE_NAME,
            transport: Transport.KAFKA,
            options: {
                run: {
                    autoCommit: true,
                },
                subscribe: {
                    fromBeginning: true
                },
                consumer: {
                    groupId: process.env.KAFKA_GROUP_ID,
                },
                client: {
                    clientId: CLIENT_MODULE_NAME,
                    brokers: ["localhost:19092", "localhost:29092", "localhost:39092"],
                }
            }
        }
    ])],
    providers: [],
    exports: [],
    controllers: [KafkaNotificationConsumer]
})
export class ClientKafkaModule {
}
