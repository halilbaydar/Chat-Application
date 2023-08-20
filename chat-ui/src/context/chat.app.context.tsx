import {createContext, JSX, useContext} from "react";


const ChatContext = createContext(null)

export default function ChatAppContext({children}: { children: JSX.Element }) {


    return (
        <ChatContext.Provider
            value={null}
        >
            {children}
        </ChatContext.Provider>
    )
}

export const ChatAppContextProvider = () => useContext(ChatContext)