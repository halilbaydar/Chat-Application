import {useEffect, useRef} from "react";
import fn = jest.fn;

export const useDidMountEffect = (func: (...args: any) => any, deps: [], run?: boolean) => {
    const didMount = useRef(false)

    useEffect(() => {
        if (didMount.current || run) {
            fn()
        } else {
            didMount.current = true
        }
    }, deps)
}