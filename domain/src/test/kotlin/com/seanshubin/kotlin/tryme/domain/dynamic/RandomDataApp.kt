package com.seanshubin.kotlin.tryme.domain.dynamic

import kotlin.random.Random

object RandomDataApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val arraySize = 5
        val mapSize = 5
        val objects = (0 until 100).map { index ->
            val random = Random(index)
            val sampleData = SampleData()
            val randomData = RandomData(random, sampleData, arraySize, mapSize)
            randomData.generateRandom(5)
        }
        val histogram = objects.fold(emptyMap(), DynamicUtil::accumulateTypeHistogram)
        display(histogram)
    }

    fun display(o:Any?){
        val json = JsonMappers.pretty.writeValueAsString(o)
        println(json)
    }
}
