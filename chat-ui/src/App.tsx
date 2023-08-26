import React, {JSX} from 'react';
import './styles/App.css';
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import Login from "./screens/login";
import Register from "./screens/register";
import Chat from "./screens/chat";
import NoPage from "./screens/no.page";
import {UserProvider} from "./context/providers/user.provider";

function App(): JSX.Element {
    return <>
        <BrowserRouter>
            <UserProvider>
                <Routes>
                    <Route path={"*"} element={<NoPage/>}></Route>
                    <Route path={"/"} element={<Navigate to={"/login"}/>}></Route>
                    <Route path={"/login"} element={<Login/>}></Route>
                    <Route path={"/register"} element={<Register/>}></Route>
                    <Route path={"/chat"} element={<Chat/>}></Route>
                </Routes>
            </UserProvider>
        </BrowserRouter>
    </>;
}

export default App;

