import {fork, takeLatest} from "redux-saga/effects";
import {SET_ACTIVE_USER} from "../saga.reducers.ts/chat.user.reducer";
import {getActiveUser} from "../watcher/saga.watch.user";
import {all} from "axios";

function* getActiveUserSaga() {
    yield takeLatest(SET_ACTIVE_USER, getActiveUser);
}

export default function* userSagas() {
    yield all([fork(getActiveUserSaga)]);
}