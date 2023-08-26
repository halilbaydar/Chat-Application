import {instance} from "./axios.factory";
import {chatStorage} from "../service/storage/chat.storage";
import {ACCESS_TOKEN, USER} from "./api.constants";
import {IUser} from "../models/req/user";
import log from "../utils/logger";
import {LoginReq} from "../models/req/login.req";
import {ErrorMessages} from "../constants/error.constant";

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
            const response = await instance.post(`/auth/login?username=${username}&password=${password}`)
            const token = response.headers["authorization"]?.split(" ")[1]
            if (!token) {
                throw new Error(ErrorMessages.TOKEN_NOT_EXISTS)
            }
            chatStorage.store(ACCESS_TOKEN, token)
            return token;
        } catch (err: any) {
            log.error("Failed to login", err)
            throw err;
        }
    }

    public async refreshLogin(): Promise<string | undefined> {
        try {
            const user = chatStorage.load(USER) as IUser
            const response = await instance.post("/refresh", {
                username: user.username,
            })
            const token = response.headers["authorization"]?.split(" ")[1]
            if (!token) {
                throw new Error(ErrorMessages.TOKEN_NOT_EXISTS)
            }
            chatStorage.store(ACCESS_TOKEN, token)
            return token;
        } catch (err: any) {
            log.error("Failed to refresh access token", err)
            throw err;
        }
    }
}