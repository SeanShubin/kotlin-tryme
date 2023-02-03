package com.seanshubin.kotlin.tryme.domain.unicode.katakana

data class KatakanaLetter(
        override val value: Char,
        override val codePoint:Int,
        val name:String,
        val romaji: String
) : Katakana
