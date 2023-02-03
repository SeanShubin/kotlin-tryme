package com.seanshubin.kotlin.tryme.domain.unicode.kanji

data class KanjiUnused(
    override val value: Char,
    override val codePoint:Int
) : Kanji