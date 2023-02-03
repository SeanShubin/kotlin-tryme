package com.seanshubin.kotlin.tryme.domain.unicode.hiragana

data class HiraganaOther(
    override val value: Char,
    override val codePoint:Int,
    val name: String
) : Hiragana
