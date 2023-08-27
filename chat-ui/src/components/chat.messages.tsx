import React from "react";
import {IMessage} from "../screens/chat";
import ChatMessageAvatar from "./chat.message.avatar";
import {UseUserContextProvider} from "../context/providers/user.provider";


export default function ChatMessages({messages, className}: { messages: Array<IMessage>, className: string }) {
    const {user} = UseUserContextProvider()!
    return <div className={className}>
        {messages?.map((message, index, messageArray) => (
            <li id={message.id} key={message.id}>
                {/*<ChatMessageAvatar message={message} index={index} messageArray={messageArray}/>*/}
                <p className={message.senderId === user.id ? "Sent Message" : "Reply Message"}>{message.text}</p>
            </li>))}
    </div>
}