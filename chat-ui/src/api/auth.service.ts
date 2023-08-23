import {instance} from "./axios.factory";
import {chatStorage} from "../service/storage/chat.storage";
import {ACCESS_TOKEN, USER} from "./api.constants";
import {IUser} from "../models/req/user";
import log from "../utils/logger";
import {LoginReq} from "../models/req/login.req";

export default class AuthService {
    private static autService: AuthService | undefined;

    public static getInstance() {
        if (!AuthService.autService) {
            AuthService.autService = new AuthService();
        }
        return AuthService.autService;
    }

    public async login({username, password}: LoginReq): Promise<string | undefined> {
        try {
            const response = await instance.post("/login", {
                username, password
            })
            const token = response.headers["Authorization"]
            chatStorage.store(ACCESS_TOKEN, token)
            return token;
        } catch (err: any) {
            log.error("Failed to refresh access token", err)
            throw err;
        }
    }

    public async refreshLogin(): Promise<string | undefined> {
        try {
            const user = chatStorage.load(USER) as IUser
            const response = await instance.post("/refresh", {
                username: user.username
            })
            const token = response.headers["Authorization"]
            chatStorage.store(ACCESS_TOKEN, token)
            return token;
        } catch (err: any) {
            log.error("Failed to refresh access token", err)
            throw err;
        }
    }
}