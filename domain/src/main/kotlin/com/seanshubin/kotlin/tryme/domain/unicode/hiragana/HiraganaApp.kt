package com.seanshubin.kotlin.tryme.domain.unicode.hiragana

object HiraganaApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val byType = HiraganaFactory.byType()
        HiraganaType.values().forEach { type ->
            val list = byType[type] ?: emptyList()
            println("$type ${list.size}")
            list.forEach(::println)
        }
    }
}