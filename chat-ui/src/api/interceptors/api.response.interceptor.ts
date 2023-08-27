import {instance} from "../axios.factory";
import {AxiosError} from "axios";
import AuthApi from "../apis/auth.api";

const authService = AuthApi.getInstance()

instance.interceptors.response.use(value => {
    return value;
}, async (error: AxiosError) => {
    if ([401, 403].includes(error.status || 200)) {
        // await authService.refreshLogin()
        console.log({error})
    }
    return error;
})