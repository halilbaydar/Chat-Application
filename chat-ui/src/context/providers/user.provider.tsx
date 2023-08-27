import {ReactComponentElement, useEffect, useMemo, useState} from "react";
import UserApi from "../../api/apis/user.api";
import {IUser} from "../../models/req/user";
import {NewContext} from "../new.context";
import {useNavigate} from "react-router-dom";

export const [UserContext, useUser] = NewContext<{ user: IUser, userList: Array<IUser> }>()

const DefaultUser: IUser = {id: "", name: "", username: ""}

export const UserProvider = ({children}: { children: ReactComponentElement<any> }) => {
    const [user, setUser] = useState<IUser>(DefaultUser)
    const [userList, setUserList] = useState<Array<IUser>>([])
    const userService = UserApi.getInstance()
    const userNavigator = useNavigate()

    useEffect(() => {
        userService.getUser()
            .then(user => {
                if (user) {
                    setUser(user)
                } else {
                    // userNavigator("/login")
                }
            })
            .catch(err => {
                // userNavigator("/login")
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
                // userNavigator("/login")
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

// export const UseUserContextProvider = () => useContext(UserContext)
export const UseUserContextProvider = () => ({
    user: {
        id: "1",
        name: "halil",
        username: "halil",
    },
    userList: [{
        id: "2",
        name: "veli",
        username: "veli"
    },
        {
            id: "3",
            name: "meli",
            username: "meli"
        },
        {
            id: "4",
            name: "meli",
            username: "meli"
        },
        {
            id: "5",
            name: "meli",
            username: "meli"
        }, {
            id: "5",
            name: "meli",
            username: "meli"
        }, {
            id: "6",
            name: "meli",
            username: "meli"
        }, {
            id: "7",
            name: "meli",
            username: "meli"
        }, {
            id: "8",
            name: "meli",
            username: "meli"
        }, {
            id: "9",
            name: "meli",
            username: "meli"
        }
    ]
})