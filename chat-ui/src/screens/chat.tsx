import React, {FormEvent, JSX, useState} from "react";
import ChatStyles from "../styles/chat.css"
import {UseUserContextProvider} from "../context/providers/user.provider";
import {IUser} from "../models/req/user";
import {Avatar, Button, Form, Input, List, message as GlobalMessageHandler} from "antd";
import {RightOutlined} from "@ant-design/icons";
import DriveFileRenameOutlineIcon from '@mui/icons-material/DriveFileRenameOutline';
import ScrollToBottom from "react-scroll-to-bottom";
// @ts-ignore
import {v4 as uuid} from 'uuid';
import {isNotEmpty} from "../utils/string.util";
import ChatTyping from "../components/chat.typing";
import ChatMessages from "../components/chat.messages";
import {useDispatch, useSelector} from "react-redux";
import {CURRENT_ACTIVE_CHAT} from "../context/sagaReducers/chat.app.reducer";

const profilePic =
    "https://static.vecteezy.com/system/resources/previews/002/318/271/original/user-profile-icon-free-vector.jpg";

export interface IMessage {
    id: string
    senderId?: string,
    text: string,
    timeStamp: Date,
    seen: boolean,
    sent: boolean,
}

export type ActiveChat = IUser

export default function Chat(props: React.HTMLProps<any>): JSX.Element {
    const {userList} = UseUserContextProvider()!
    const [activeChat, setActiveChat] = useState<ActiveChat>()
    const [message, setMessage] = useState("")
    const [messages, setMessages] = useState<Array<IMessage>>([])
    const [typing, setTyping] = useState(true)

    const currentChat = useSelector((state: any) => state.chatReducer.activeChat)
    const dispatch = useDispatch()

    const [form] = Form.useForm();

    const openChat = (event: React.MouseEvent<HTMLDivElement, MouseEvent>, chat: IUser) => {
        event.preventDefault();
        setActiveChat(chat)
        setMessages([])
        dispatch({type: CURRENT_ACTIVE_CHAT, payload: activeChat})
    }

    const handleSendMessage = (e: FormEvent<HTMLElement>) => {
        if (activeChat) {
            if (isNotEmpty(message)) {
                const newMessage = {
                    id: uuid(),
                    senderId: "1",
                    text: message,
                    timeStamp: new Date(),
                    seen: true,
                    sent: false
                }
                setMessages([
                    ...messages,
                    newMessage,
                ])
                setMessage("")
                form.resetFields()
            }
        } else {
            GlobalMessageHandler.error("Please select a chat...")
        }
    }

    const typingHandler = (value: string) => {
        setMessage(value)
    }

    return (<div className="ChatContainer" style={ChatStyles.ChatContainer}>
        <div className="SidePanel" style={ChatStyles.SidePanel}>
            <List className="ChatListContainer" style={ChatStyles.ChatListContainer}
                  dataSource={userList}
                  renderItem={(chat, index) => (
                      <List.Item
                          key={chat.username}
                          onClick={event => openChat(event, chat)}
                      >
                          <div style={{display: "flex", flexDirection: "column"}}>
                              <Avatar src={`https://xsgames.co/randomusers/avatar.php?g=pixel&key=${index}`}
                                      style={chat.id === activeChat?.id ?
                                          {marginLeft: "0", float: "left", border: "2px solid blue"} :
                                          {marginLeft: "0", float: "left"}}/>
                              <List.Item.Meta
                                  title={chat.name.length > 3 ? chat.name.slice(0, 3).concat("..") : chat.name}
                              />
                          </div>
                      </List.Item>
                  )}
            />
        </div>
        <div className="UnSidePanel" style={ChatStyles.UnSidePanel}>
            <div className="ChatHeader" style={ChatStyles.ChatHeader}>
                <div className="Title">
                    <img className="SenderImage"
                         style={ChatStyles.SenderImage}
                         src={profilePic} alt=""
                    />
                    <span className="ActiveChatUser">
                        {activeChat ? activeChat.name : "Chat"}
                    </span>
                </div>
            </div>
            <ScrollToBottom className="MessagesContainer">
                <ul>
                    <ChatMessages messages={messages} className="Messages"/>
                    <ChatTyping typing={typing}/>
                </ul>
            </ScrollToBottom>
            <Form
                className="MessageInputContainer"
                form={form}
                onFinish={handleSendMessage}
            >
                <Form.Item name="message">
                    <Input
                        prefix={<DriveFileRenameOutlineIcon/>}
                        placeholder="type here..."
                        onChange={e => typingHandler(e.target.value)}
                        suffix={<Button
                            shape="circle"
                            icon={<RightOutlined/>}
                            onClick={handleSendMessage}/>
                        }
                    />
                </Form.Item>
            </Form>
        </div>
    </div>)
}