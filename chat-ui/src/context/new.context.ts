import {Context, createContext, useContext} from "react";

export const NewContext = <T>(): [Context<T | undefined>, () => T] => {
    const ctx = createContext<T | undefined>(undefined);
    const useCtx = () => {
        const ctxValue = useContext(ctx);
        if (ctxValue === undefined) {
            throw new Error();
        }
        return ctxValue;
    };

    return [ctx, useCtx];
};