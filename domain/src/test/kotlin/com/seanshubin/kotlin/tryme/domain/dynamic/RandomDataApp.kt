package com.seanshubin.kotlin.tryme.domain.dynamic

import com.seanshubin.kotlin.tryme.domain.json.JsonMappers
import kotlin.random.Random

object RandomDataApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val random = Random(0)
        val sampleData = SampleData()
        val arraySize = 5
        val mapSize = 5
        val randomData = RandomData(sampleData, arraySize, mapSize)
        val o = randomData.generateRandom(5)
//        display(o)
        val flattened = DynamicUtil.flattenMap(o){ a, b -> "$a.$b"}
        display(flattened)
    }


    fun display(o:Any?){
        val json = JsonMappers.pretty.writeValueAsString(o)
        println(json)
    }
}
