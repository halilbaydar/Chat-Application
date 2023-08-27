import {combineReducers} from "redux";
import {chatReducer} from "./chat.app.reducer";
import {chatUserReducer} from "./chat.user.reducer";
import createSagaMiddleware from "redux-saga";
import {configureStore} from "@reduxjs/toolkit";
import rootSaga from "../sagas/saga.root";

const reducers = combineReducers({
    chatReducer,
    chatUserReducer
})

const sagaMiddleware = createSagaMiddleware()

export const ChatStore = configureStore({
    reducer: reducers,
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware().concat(sagaMiddleware)
})

sagaMiddleware.run(rootSaga)