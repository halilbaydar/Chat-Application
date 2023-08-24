import {ConsumerDeserializer} from "@nestjs/microservices/interfaces/deserializer.interface";

export class CustomDeserializer implements ConsumerDeserializer {
    public deserialize(message: any, options?: Record<string, any>) {
        console.log({message, options})
        return message;
    }

}
