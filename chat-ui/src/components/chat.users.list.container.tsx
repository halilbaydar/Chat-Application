import React from "react";
import {UseUserContext} from "../context/providers/user.provider";

export default function ChatUsersListContainer() {
    const {userList} = UseUserContext()!

}