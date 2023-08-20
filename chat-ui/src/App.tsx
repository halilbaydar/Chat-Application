import React, {JSX} from 'react';
import './styles/App.css';
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import Login from "./screens/login";
import Register from "./screens/register";
import Chat from "./screens/chat";
import NoPage from "./screens/no.page";

function App(): JSX.Element {
    return <BrowserRouter>
        <Routes>
            <Route path={"*"} element={<NoPage/>}></Route>
            <Route path={"/"} element={<Navigate to={"/login"} />}></Route>
            <Route path={"/login"} element={<Login/>}></Route>
            <Route path={"/register"} element={<Register/>}></Route>
            <Route path={"/chat"} element={<Chat/>}></Route>
        </Routes>
    </BrowserRouter>;
}

export default App;

