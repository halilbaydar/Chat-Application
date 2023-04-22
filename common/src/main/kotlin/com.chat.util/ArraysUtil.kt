package com.chat.util

import java.util.Arrays


fun <T> Arrays.fromString(list: String): List<String> {
    return list.substring(1, list.length - 1).split(",")
}