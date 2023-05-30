package com.chat.consumer

import java.io.Serializable


interface RMessageConsumer<M : Serializable?, R> {
    @Throws(Exception::class)
    fun consume(m: M): R
}
