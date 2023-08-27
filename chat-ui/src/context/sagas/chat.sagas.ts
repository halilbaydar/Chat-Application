import {all, fork, takeLatest} from "redux-saga/effects";
import {ALL_ACTIVE_CHATS, CURRENT_ACTIVE_CHAT} from "../sagaReducers/chat.app.reducer";
import {getAllChats, getChatDetail} from "../watcher/saga.watch.chat";

function* getAllActiveChatsSaga() {
    yield takeLatest(ALL_ACTIVE_CHATS, getAllChats);
}

function* getActiveChat() {
    yield takeLatest(CURRENT_ACTIVE_CHAT, getChatDetail);
}

export default function* chatSagas() {
    yield all([fork(getActiveChat), fork(getAllActiveChatsSaga)]);
}
