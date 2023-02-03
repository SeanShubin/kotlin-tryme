package com.seanshubin.kotlin.tryme.domain.unicode.katakana

import com.seanshubin.kotlin.tryme.domain.unicode.katakana.Katakana

data class KatakanaLetterSmall(
    override val value: Char,
    val romaji: String,
    val name:String
) : Katakana