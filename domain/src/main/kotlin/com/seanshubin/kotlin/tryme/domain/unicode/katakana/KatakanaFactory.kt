package com.seanshubin.kotlin.tryme.domain.unicode.katakana

object KatakanaFactory {
    fun Char.toKatakana(): Katakana {
        val katakana = katakanaTypeStream.map { it.create(this) }.find { it != null }
        return katakana!!
    }

    fun Char.toTypeAndKatakana(): Pair<KatakanaType, Katakana> {
        val pair = katakanaTypeStream.map {
            val hiragana = it.create(this)
            if (hiragana == null) null
            else it to hiragana
        }.find { it != null }
        return pair!!
    }

    fun values(): List<Katakana> = range.map { it.toKatakana() }

    fun byType(): Map<KatakanaType, List<Katakana>> =
        range.map { char ->
            char.toTypeAndKatakana()
        }.groupBy { pair ->
            pair.first
        }.map { (type, pairs) ->
            type to pairs.map { pair ->
                pair.second
            }
        }.toMap()

    private const val unicodeRangeFirst = '\u30A0'
    private const val unicodeRangeLast = '\u30FF'
    private val range = unicodeRangeFirst..unicodeRangeLast
    private val katakanaTypeStream = sequenceOf(*KatakanaType.values())
}
