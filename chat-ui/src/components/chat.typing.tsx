import React from "react";
import TypingImage from "../assets/gif/typing.gif";

export default function ChatTyping({typing}: { typing: boolean }) {
    if (typing) {
        return <li
            id="typing"
            style={{display: "none", marginBottom: "15px"}}
            className="Reply"
        >
            <img src={TypingImage} alt=""/>
        </li>
    }
    return <></>
}