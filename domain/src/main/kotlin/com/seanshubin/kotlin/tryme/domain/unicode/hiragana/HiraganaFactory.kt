package com.seanshubin.kotlin.tryme.domain.unicode.hiragana

object HiraganaFactory {
    fun Char.toHiragana(): Hiragana {
        val hiragana = hiraganaTypeStream.map { it.create(this) }.find { it != null }
        return hiragana!!
    }

    fun Char.toTypeAndHiragana(): Pair<HiraganaType, Hiragana> {
        val pair = hiraganaTypeStream.map {
            val hiragana = it.create(this)
            if (hiragana == null) null
            else it to hiragana
        }.find { it != null }
        return pair!!
    }

    fun values(): List<Hiragana> = range.map { it.toHiragana() }

    fun byType(): Map<HiraganaType, List<Hiragana>> =
        range.map { char ->
            char.toTypeAndHiragana()
        }.groupBy { pair ->
            pair.first
        }.map { (type, pairs) ->
            type to pairs.map { pair ->
                pair.second
            }
        }.toMap()

    private const val unicodeRangeFirst = '\u3040'
    private const val unicodeRangeLast = '\u309F'
    private val range = unicodeRangeFirst..unicodeRangeLast
    private val hiraganaTypeStream = sequenceOf(*HiraganaType.values())
}