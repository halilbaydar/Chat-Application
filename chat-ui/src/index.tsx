import React from 'react';
import ReactDOM from 'react-dom/client';
import './styles/index.css';
import App from './App';
import ChatAppContext from "./context/chat.app.context";

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);
root.render(
    <React.StrictMode>
        <ChatAppContext>
            <App/>
        </ChatAppContext>
    </React.StrictMode>
);
