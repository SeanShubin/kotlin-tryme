package com.seanshubin.kotlin.tryme.domain.receiver

import com.seanshubin.kotlin.tryme.domain.receiver.BodyTag.Body

class Html : TagWithText("html") {
    fun head(init: Head.() -> Unit) = initTag(Head(), init)

    fun body(init: Body.() -> Unit) = initTag(Body(), init)
}
