package com.seanshubin.kotlin.tryme.domain.unicode.hiragana

object HiraganaApp {
    @JvmStatic
    fun main(args: Array<String>) {
        HiraganaFactory.values().filterIsInstance<HiraganaLetter>().forEach{
            println("${it.value} ${it.romaji} ${it.name}")
        }
    }
}