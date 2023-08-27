import {call, put} from 'redux-saga/effects'
import ChatApi from "../../api/apis/chat.api";
import {
    ALL_ACTIVE_CHATS,
    CURRENT_ACTIVE_CHAT,
    FETCH_ALL_ACTIVE_CHATS_FAILED,
    FETCH_CURRENT_ACTIVE_CHAT_FAILED,
    GetActiveChatAction
} from "../sagaReducers/chat.app.reducer";

export function* getAllChats(action: any): any {
    const chatApi = ChatApi.getInstance();
    try {
        const chats = yield call(chatApi.getChats)
        yield put({type: ALL_ACTIVE_CHATS, payload: chats})
    } catch (error: any) {
        yield put({type: FETCH_ALL_ACTIVE_CHATS_FAILED, message: error.message});
    }
}

export function* getChatDetail({id}: GetActiveChatAction): any {
    const chatApi = ChatApi.getInstance();
    try {
        const chatDetail = yield call(chatApi.getChatDetail, id)
        yield put({type: CURRENT_ACTIVE_CHAT, payload: chatDetail})
    } catch (error: any) {
        yield put({type: FETCH_CURRENT_ACTIVE_CHAT_FAILED, message: error.message});
    }
}
