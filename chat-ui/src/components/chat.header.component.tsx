import React, {JSX} from "react";
import ChatHeaderStyles from '../styles/header.css'

export default function ChatHeader(): JSX.Element {
    return <header className="chat-header" style={ChatHeaderStyles.chatHeader}>
        Chat App
    </header>
}