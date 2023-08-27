import {Action} from "../action";

export const CURRENT_ACTIVE_CHAT = "activeChat";
export const FETCH_CURRENT_ACTIVE_CHAT_FAILED = "fetchActiveChatFailed";
export const ALL_ACTIVE_CHATS = "allActiveChats";
export const FETCH_ALL_ACTIVE_CHATS_FAILED = "fetchAllActiveChatsFailed";

export interface GetActiveChatAction {
    type: typeof CURRENT_ACTIVE_CHAT;
    id: string;
}

export const chatReducer = (state: any, action: Action) => {
    switch (action.type) {
        case CURRENT_ACTIVE_CHAT: {
            return {
                ...state,
                activeChat: action.payload
            }
        }
        case ALL_ACTIVE_CHATS: {
            return {
                ...state,
                allActiveChats: action.payload
            }
        }
        case FETCH_ALL_ACTIVE_CHATS_FAILED: {
            return {
                ...state,
                fetchAllActiveChatsFailed: action.payload
            }
        }
        case FETCH_CURRENT_ACTIVE_CHAT_FAILED: {
            return {
                ...state,
                fetchActiveChatFailed: action.payload
            }
        }
        default : {
            return {
                ...state,
                allActiveChats: {}
            }
        }
    }
}
