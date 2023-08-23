import React, {JSX, useState} from "react";
import LoginStyles from "../styles/login.css";
import ChatHeader from "../components/chat.header.component";
import {Button, Form, Input, message} from "antd";
import {LockOutlined, UserOutlined} from "@ant-design/icons";
import {RegisterReq} from "../models/req/register.req";
import {useNavigate} from "react-router-dom";
import UserService from "../api/user.service";

export default function Register(): JSX.Element {
    const [loading, setLoading] = useState(false)
    const navigate = useNavigate()
    const userService = UserService.getInstance();

    const onFinish = (values: RegisterReq) => {
        setLoading(true)
        userService.register(values)
            .then(response => {
                navigate("/login")
            })
            .catch(error => {
                message.error(`Credentials are wrong. Message: ${error.message}`)
            })
            .finally(() => {
                setLoading(false)
            })
    }

    return <div className="LoginContainer" style={LoginStyles.LoginContainer}>
        <ChatHeader/>
        <Form
            initialValues={{remember: true}}
            onFinish={onFinish}
        >
            <Form.Item
                name="username"
                rules={[{
                    required: true, message: "Username is mandatory...!!!"
                }]}
            >
                <Input
                    prefix={<UserOutlined className="site-form-item-icon"/>}
                    placeholder="username"
                />
            </Form.Item>
            <Form.Item
                name="password"
                rules={[{
                    required: true, message: "Password is mandatory...!!!"
                }]}
            >
                <Input
                    prefix={<LockOutlined className="site-form-item-icon"/>}
                    placeholder="password"
                    type="password"
                />
            </Form.Item>
            <Form.Item>
                <Button
                    shape="round"
                    type="primary"
                    size="large"
                    htmlType="submit"
                    loading={loading}
                >
                    Login
                </Button>
            </Form.Item>
        </Form>
    </div>
}