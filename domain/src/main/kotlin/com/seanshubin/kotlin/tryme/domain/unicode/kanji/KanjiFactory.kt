package com.seanshubin.kotlin.tryme.domain.unicode.kanji

object KanjiFactory {
    fun Char.toKanji(): Kanji {
        val kanji = kanjiTypeStream.map { it.create(this) }.find { it != null }
        return kanji!!
    }

    fun Char.toTypeAndKanji(): Pair<KanjiType, Kanji> {
        val pair = kanjiTypeStream.map {
            val hiragana = it.create(this)
            if (hiragana == null) null
            else it to hiragana
        }.find { it != null }
        return pair!!
    }

    fun values(): List<Kanji> = range.map { it.toKanji() }

    fun byType(): Map<KanjiType, List<Kanji>> =
        range.map { char ->
            char.toTypeAndKanji()
        }.groupBy { pair ->
            pair.first
        }.map { (type, pairs) ->
            type to pairs.map { pair ->
                pair.second
            }
        }.toMap()
    private const val unicodeRangeFirst = '\u4E00'
    private const val unicodeRangeLast = '\u9FAF'
    private val range = unicodeRangeFirst..unicodeRangeLast
    private val kanjiTypeStream = sequenceOf(*KanjiType.values())
}
