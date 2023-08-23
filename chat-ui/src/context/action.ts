export type ActionType = "user" | "chat" | "messages"

export interface Action<T = any> {
    type: ActionType,
    payload: T
}