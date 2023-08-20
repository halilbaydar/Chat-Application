import {InternalAxiosRequestConfig} from "axios/index";
import {instance} from "../axios.factory";

instance.interceptors.request.use(async (options: InternalAxiosRequestConfig<unknown>) => {
    const token = localStorage.getItem("accessToken");
    if (token) {
        options.headers.Authorization = `Bearer ${token}`;
    }
    return options;
});