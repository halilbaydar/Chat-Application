import {Action} from "./action";

export const chatAppReducer = (state: any, action: Action) => {
    switch (action.type) {
        case "user": {
            return {
                ...state,
                user: action.payload
            }
        }
    }
}
