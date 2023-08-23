import {LoginReq} from "./login.req";

export interface RegisterReq extends LoginReq {
    name: string
}