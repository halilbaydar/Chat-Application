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
            const {data} = await instance.post("/user/v1/register", {
                username, name: username, password
            })
            return data;
        } catch (err: any) {
            log.error("Failed to register", err)
            throw err;
        }
    }

    public async getUser(): Promise<IUser | undefined> {
        try {
            const {data} = await instance.get("/user")
            return data
        } catch (err) {
            log.error("Failed to get user from server", err)
            throw err
        }
    }
}