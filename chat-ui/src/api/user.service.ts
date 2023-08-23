import {instance} from "./axios.factory";
import {IUser} from "../models/req/user";
import log from "../utils/logger";
import {LoginReq} from "../models/req/login.req";

export default class UserService {
    private static userService: UserService | undefined;

    public static getInstance() {
        if (!UserService.userService) {
            UserService.userService = new UserService();
        }
        return UserService.userService;
    }

    public async register({username, password}: LoginReq): Promise<string | undefined> {
        try {
            const {data} = await instance.post("/register", {
                username, password
            })
            return data;
        } catch (err: any) {
            log.error("Failed to refresh access token", err)
            throw err;
        }
    }

    public async getUser(): Promise<IUser | undefined> {
        try {
            const {data} = await instance.get("/user")
            return data as IUser;
        } catch (err) {
            log.error("Failed to get user from server", err)
            throw err
        }
    }
}