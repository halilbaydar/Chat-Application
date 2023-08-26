import {instance} from "../axios.factory";
import {AxiosError} from "axios";
import AuthService from "../auth.service";

const authService = AuthService.getInstance()

instance.interceptors.response.use(value => {
    return value;
}, async (error: AxiosError) => {
    if ([401, 403].includes(error.status || 200)) {
        // await authService.refreshLogin()
        console.log({error})
    }
    return error;
})