import {hasCookieAccess} from "../../api/cookie/cookie.access.check";

const chatLocalStorage: { [key: string]: any } = {}
const store = (key: string, value: string) => {
    if (hasCookieAccess()) {
        localStorage.setItem(key, value)
    }
    chatLocalStorage[key] = value
}

const load = (key: string) => {
    if (hasCookieAccess()) {
        return localStorage.getItem(key)
    }
    return chatLocalStorage[key]
}

const erase = (key: string, value: string) => {
    if (hasCookieAccess()) {
        localStorage.removeItem(key)
    }
    delete chatLocalStorage[key]
}

export const chatStorage = {
    store, load, erase
}