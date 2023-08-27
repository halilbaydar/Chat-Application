import {Action} from "../action";

export const SET_ACTIVE_USER = "setActiveUser";
export const USER_ACTIVE_FETCH_FAILED = "setActiveUserFailed";
export const chatUserReducer = (state: any, action: Action) => {
    switch (action.type) {
        case SET_ACTIVE_USER: {
            return {
                ...state,
                activeUser: action.payload
            }
        }
        case USER_ACTIVE_FETCH_FAILED: {
            return {
                ...state,
                setActiveUserFailed: action.payload
            }
        }
        default : {
            return {
                ...state,
                activeUser: {}
            }
        }
    }
}
