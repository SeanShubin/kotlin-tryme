package com.seanshubin.kotlin.tryme.domain.unicode.hiragana

object HiraganaFactory {
    fun Char.toHiragana(): Hiragana {
        val hiragana = hiraganaTypeStream.map { it.create(this) }.find { it != null }
        return hiragana!!
    }

    fun values(): List<Hiragana> = range.map { it.toHiragana() }

    private const val unicodeRangeFirst = '\u3040'
    private const val unicodeRangeLast = '\u309F'
    private val range = unicodeRangeFirst..unicodeRangeLast
    private val hiraganaTypeStream = sequenceOf(*HiraganaType.values())
}