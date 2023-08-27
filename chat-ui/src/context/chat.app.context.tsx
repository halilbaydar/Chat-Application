import React, {createContext, JSX, useContext, useReducer} from "react";
import {Action} from "./action";
import {chatReducer} from "./sagaReducers/chat.app.reducer";

const ChatContext = createContext<{ state: any, dispatch: React.Dispatch<Action> } | null>(null)

export default function ChatAppContext({children}: { children: JSX.Element }) {

    const [state, dispatch] = useReducer(chatReducer, {})

    return (
        <ChatContext.Provider
            value={{state, dispatch}}
        >
            {children}
        </ChatContext.Provider>
    )
}

export const ChatAppContextProvider = () => useContext(ChatContext)