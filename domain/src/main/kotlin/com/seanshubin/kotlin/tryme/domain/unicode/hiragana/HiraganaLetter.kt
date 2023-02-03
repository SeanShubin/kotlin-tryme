package com.seanshubin.kotlin.tryme.domain.unicode.hiragana

data class HiraganaLetter(
    override val value: Char,
    val romaji: String,
    val name:String
) : Hiragana