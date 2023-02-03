package com.seanshubin.kotlin.tryme.domain.unicode.hiragana

data class HiraganaLetterSmall(
    override val value: Char,
    override val codePoint:Int,
    val name:String,
    val romaji: String
) : Hiragana