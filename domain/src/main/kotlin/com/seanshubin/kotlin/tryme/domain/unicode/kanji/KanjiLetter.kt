package com.seanshubin.kotlin.tryme.domain.unicode.kanji

data class KanjiLetter(
    override val value: Char,
    override val codePoint:Int,
    val name: String,
    val id:String
) : Kanji
