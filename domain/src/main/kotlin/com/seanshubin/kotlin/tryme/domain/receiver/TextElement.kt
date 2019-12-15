package com.seanshubin.kotlin.tryme.domain.receiver

class TextElement(val text: String) : Element {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$text\n")
    }
}
