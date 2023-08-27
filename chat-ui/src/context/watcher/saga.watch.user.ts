import {call, put} from 'redux-saga/effects'
import UserApi from "../../api/apis/user.api";
import {SET_ACTIVE_USER} from "../saga.reducers.ts/chat.user.reducer";

export function* getActiveUser(action: any): any {
    const userService = UserApi.getInstance();
    try {
        const user = yield call(userService.getUser)
        yield put({type: SET_ACTIVE_USER, payload: user})
    } catch (error: any) {
        yield put({type: "", message: error.message});
    }
}
