package com.seanshubin.kotlin.tryme.domain.unicode.hiragana

data class HiraganaLetterSmall(
    override val value: Char,
    val romaji: String,
    val name:String
) : Hiragana