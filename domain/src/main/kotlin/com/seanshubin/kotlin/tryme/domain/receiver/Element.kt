package com.seanshubin.kotlin.tryme.domain.receiver

interface Element {
    fun render(builder: StringBuilder, indent: String)
}

