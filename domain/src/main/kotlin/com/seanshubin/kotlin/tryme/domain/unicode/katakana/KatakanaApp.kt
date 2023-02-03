package com.seanshubin.kotlin.tryme.domain.unicode.katakana

object KatakanaApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val byType = KatakanaFactory.byType()
        KatakanaType.values().forEach { type ->
            val list = byType[type] ?: emptyList()
            println("$type ${list.size}")
//            list.forEach(::println)
        }
    }
}