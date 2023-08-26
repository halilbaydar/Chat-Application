import {instance} from "./axios.factory";
import {IUser} from "../models/req/user";
import log from "../utils/logger";
import {LoginReq} from "../models/req/login.req";
import {USER_ROUTER_PREFIX} from "../constants/router.prefix";
import {ErrorMessages} from "../constants/error.constant";

export default class UserService {
    private static userService: UserService | undefined;

    private constructor() {
        if (UserService.userService) {
            throw new Error(ErrorMessages.USER_INSTANCE_ALREADY_EXISTS)
        }
    }

    public static getInstance() {
        if (!UserService.userService) {
            UserService.userService = new UserService();
        }
        return UserService.userService;
    }

    public async register({username, password}: LoginReq): Promise<string | undefined> {
        try {
            const {data} = await instance.post(`${USER_ROUTER_PREFIX}/v1/register`, {
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
            const {data} = await instance.get(`${USER_ROUTER_PREFIX}/user`)
            return data
        } catch (err) {
            log.error("Failed to get user from server", err)
            throw err
        }
    }

    public async getUsers(): Promise<Array<IUser> | undefined> {
        try {
            const {data} = await instance.get(`${USER_ROUTER_PREFIX}/v1/user/list`)
            return data
        } catch (err) {
            log.error("Failed to get user from server", err)
            throw err
        }
    }
}