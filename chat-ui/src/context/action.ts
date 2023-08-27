export type ActionType =
    "user"
    | "chat"
    | "messages"
    | "activeChat"
    | "setActiveUser"
    | "activeUser"
    | "allActiveChats"
    | "fetchAllActiveChatsFailed"
    | "setActiveUserFailed"
    | "fetchActiveChatFailed"

export interface Action<T = any> {
    type: ActionType,
    payload: T
}