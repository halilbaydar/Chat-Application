import {instance} from "../api/axios.factory";
import {isBlank} from "../utils/string.util";
import {LoginReq} from "../models/req/login.req";
import {AxiosResponse} from "axios/index";

export default async function LoginHandler({username, password}: LoginReq): Promise<AxiosResponse> {
    if (isBlank(username) || isBlank(password)) {
        throw new Error("Please fill all fields...");
    }

    return new Promise((resolve, reject) => {
        instance.post("/login", {
            username, password
        }).then((response: AxiosResponse) => {
            resolve(response)
        }).catch(error => {
            reject(error)
        })
    });
}