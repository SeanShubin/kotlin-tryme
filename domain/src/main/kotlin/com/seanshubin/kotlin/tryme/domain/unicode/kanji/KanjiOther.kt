package com.seanshubin.kotlin.tryme.domain.unicode.kanji

data class KanjiOther(
    override val value: Char,
    override val codePoint:Int,
    val name: String
) : Kanji
