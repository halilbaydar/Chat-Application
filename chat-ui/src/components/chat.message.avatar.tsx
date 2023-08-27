import React from "react";
import {IMessage} from "../screens/chat";
import {Avatar} from "antd";
import {UseUserContextProvider} from "../context/providers/user.provider";

interface Props {
    message: IMessage,
    index: number,
    messageArray: Array<IMessage>,
}

export default function ChatMessageAvatar({message, index, messageArray}: Props) {
    const {user} = UseUserContextProvider()!

    const avatar = <Avatar src={`https://xsgames.co/randomusers/avatar.php?g=pixel&key=${index}`}
                           style={{marginLeft: "0", float: "left"}}/>;


    if (message.senderId === user.id) {
        return <></>
    }

    if (index === 0) {
        return avatar
    }

    const previousMessage = messageArray[index - 1]
    if (previousMessage.senderId === message.senderId) {
        return <></>
    }

    return avatar
}