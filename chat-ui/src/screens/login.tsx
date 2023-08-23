import React, {JSX, useState} from "react";
import LoginStyles from '../styles/login.css'
import {Button, Form, Input, message} from "antd";
import {LockOutlined, UserOutlined} from "@ant-design/icons";
import {useNavigate} from "react-router-dom";
import ChatHeader from "../components/chat.header.component";
import AuthService from "../api/auth.service";

interface LoginFormValues {
    username: string,
    password: string
}

export default function Login(): JSX.Element {
    const [loading, setLoading] = useState(false)
    const navigate = useNavigate()
    const authService = AuthService.getInstance();

    const onFinish = (values: LoginFormValues) => {
        setLoading(true)
        authService.login(values)
            .then(response => {
                navigate("/chat")
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