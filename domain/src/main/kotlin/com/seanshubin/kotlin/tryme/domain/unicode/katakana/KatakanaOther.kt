package com.seanshubin.kotlin.tryme.domain.unicode.katakana

import com.seanshubin.kotlin.tryme.domain.unicode.katakana.Katakana

data class KatakanaOther(
    override val value: Char,
    override val codePoint: Int,
    val name: String
) : Katakana
