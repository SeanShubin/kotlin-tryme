package com.seanshubin.kotlin.tryme.domain.dice

import java.nio.file.Files
import java.nio.file.Paths

object Encode {
    @JvmStatic
    fun main(args: Array<String>) {
        val pathName = "domain/src/main/kotlin/com/seanshubin/kotlin/tryme/domain/dice/SpheresOfInfluenceDiceStatisticsApp.kt"
        val path = Paths.get(pathName)
        val text = Files.readString(path)
        val encoded = text
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("&", "&amp;")
        println(encoded)
    }
}