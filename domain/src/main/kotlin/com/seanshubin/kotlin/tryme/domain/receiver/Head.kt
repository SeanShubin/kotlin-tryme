package com.seanshubin.kotlin.tryme.domain.receiver

class Head : TagWithText("head") {
    fun title(init: Title.() -> Unit) = initTag(Title(), init)
}
