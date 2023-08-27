import {instance} from "../axios.factory";
import log from "../../utils/logger";
import {CHAT_ROUTER_PREFIX} from "../../constants/router.prefix";
import {ErrorMessages} from "../../constants/error.constant";

export default class ChatApi {
    private static chatApi: ChatApi | undefined;

    private constructor() {
        if (ChatApi.chatApi) {
            throw new Error(ErrorMessages.CHAT_API_INSTANCE_ALREADY_EXISTS)
        }
    }

    public static getInstance() {
        if (!ChatApi.chatApi) {
            ChatApi.chatApi = new ChatApi();
        }
        return ChatApi.chatApi;
    }

    public async getChats(): Promise<string | undefined> {
        try {
            const {data} = await instance.post(`${CHAT_ROUTER_PREFIX}/v1/chat/list`, {})
            return data;
        } catch (err: any) {
            log.error("Failed to get all chats", err)
            throw err;
        }
    }

    public async getChatDetail(id: string): Promise<string | undefined> {
        try {
            const {data} = await instance.post(`${CHAT_ROUTER_PREFIX}/v1/chat/list/${id}`, {})
            return data;
        } catch (err: any) {
            log.error(`Failed to get chat detail of id: ${id}`, err)
            throw err;
        }
    }
}