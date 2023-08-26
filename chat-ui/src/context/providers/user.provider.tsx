import {ReactComponentElement, useContext, useEffect, useMemo, useState} from "react";
import UserService from "../../api/user.service";
import {IUser} from "../../models/req/user";
import {NewContext} from "../new.context";
import {useNavigate} from "react-router-dom";

export const [UserContext, useUser] = NewContext<{ user: IUser | undefined, userList: Array<IUser> }>()

export const UserProvider = ({children}: { children: ReactComponentElement<any> }) => {
    const [user, setUser] = useState<IUser>()
    const [userList, setUserList] = useState<Array<IUser>>([])
    const userService = UserService.getInstance()
    const userNavigator = useNavigate()

    useEffect(() => {
        userService.getUser()
            .then(user => {
                if (user) {
                    setUser(user)
                } else {
                    userNavigator("/login")
                }
            })
            .catch(err => {
                userNavigator("/login")
            })
    })

    useEffect(() => {
        userService.getUsers()
            .then(users => {
                if (users) {
                    setUserList(users)
                } else {
                    setUserList([])
                }
            })
            .catch(err => {
                userNavigator("/login")
            })
    })

    const userMemoizedValue = useMemo(() => ({
        user, userList
    }), [user, userList])

    return <UserContext.Provider
        value={userMemoizedValue}>
        {children}
    </UserContext.Provider>
}

export const UseUserContext = () => useContext(UserContext)