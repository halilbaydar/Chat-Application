import {all, fork} from "redux-saga/effects";
import userSagas from "./user.sagas";
import chatSagas from "./chat.sagas";

export default function* rootSaga() {
    yield all([fork(chatSagas), fork(userSagas)]);
}
