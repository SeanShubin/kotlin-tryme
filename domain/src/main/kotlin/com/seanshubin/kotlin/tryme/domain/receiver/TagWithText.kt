package com.seanshubin.kotlin.tryme.domain.receiver

abstract class TagWithText(name: String) : Tag(name) {
    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }
}
