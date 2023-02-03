package com.seanshubin.kotlin.tryme.domain.unicode.kanji

object KanjiApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val byType = KanjiFactory.byType()
        KanjiType.values().forEach { type ->
            val list = byType[type] ?: emptyList()
            println("$type ${list.size}")
            list.forEach(::println)
        }
    }
}
