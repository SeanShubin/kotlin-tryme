package com.seanshubin.kotlin.tryme.domain.unicode.katakana

import com.seanshubin.kotlin.tryme.domain.unicode.katakana.Katakana

data class KatakanaLetterSmall(
    override val value: Char,
    override val codePoint: Int,
    val name:String,
    val romaji: String
) : Katakana