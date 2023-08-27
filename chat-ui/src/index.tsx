import React from 'react';
import ReactDOM from 'react-dom/client';
import './styles/index.css';
import App from './App';
import {Provider} from "react-redux";
import {ChatStore} from "./context/sagaReducers/root.reducer";

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);
root.render(<Provider store={ChatStore}>
    <App/>
</Provider>);