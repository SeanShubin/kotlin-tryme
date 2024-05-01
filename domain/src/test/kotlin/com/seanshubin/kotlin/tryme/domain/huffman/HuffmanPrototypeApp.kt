package com.seanshubin.kotlin.tryme.domain.huffman

import com.seanshubin.kotlin.tryme.domain.statistics.StatisticsForListOfDouble.median
import com.seanshubin.kotlin.tryme.domain.statistics.StatisticsForListOfDouble.standardDeviation
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals

object HuffmanPrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val fileName = args[0]
        val path = Paths.get(fileName)
        val links = Files.readAllLines(path).map(::removeDomain)
        val codeTable = HuffmanCodeTable.buildCodeTable(links)
        val compressionValues = links.map{ link ->
            val encoded = codeTable.encode(link)
            val decoded = codeTable.decode(encoded)
            assertEquals(link, decoded)
            println("(${link.length}) $link")
            println("(${encoded.length}) $encoded")
            val compression = encoded.length.toDouble() / link.length.toDouble() * 100
            println(compression)
            compression
        }
        val mean = compressionValues.sum() / compressionValues.size
        val median = compressionValues.median()
        val min = compressionValues.min()
        val max = compressionValues.max()
        val standardDeviation = compressionValues.standardDeviation()
        println("mean = $mean, median = $median, min = $min, max = $max, standardDeviation = $standardDeviation")
        println(listOf(3.0,5.0,6.0,7.0,9.0).standardDeviation())
    }

    fun removeDomain(s:String):String {
        val lastSlashIndex  = s.lastIndexOf('/')
        return s.substring(lastSlashIndex+1)
    }
}