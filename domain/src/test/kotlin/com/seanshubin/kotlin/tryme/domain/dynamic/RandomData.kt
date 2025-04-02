package com.seanshubin.kotlin.tryme.domain.dynamic

import kotlin.random.Random

class RandomData(
    private val sampleData:SampleData,
    private val arraySize:Int,
    private val mapSize:Int
) {
    fun generateRandom(depth: Int): Any? {
        val theType = lookupType(depth)
        return when (theType) {
            JsonType.OBJECT -> generateObject(depth)
            JsonType.ARRAY -> generateArray(depth)
            JsonType.STRING -> sampleData.string()
            JsonType.NUMBER -> sampleData.int()
            JsonType.BOOLEAN -> sampleData.boolean()
            JsonType.NULL -> null
        }
    }

    private fun lookupType(depth: Int): JsonType {
        val frequencies =
            if (depth < 0) shallowFrequencies
            else defaultFrequencies
        val randomRange = frequencies.values.sum()
        val lookupByFrequency = LookupByFrequency(frequencies)
        val index = random.nextInt(randomRange)
        val theType = lookupByFrequency.lookup(index)
        return theType
    }

    fun generateObject(depth: Int): Map<Any?, Any?> {
        return (0 until mapSize).map {
            sampleData.string() to generateRandom(depth-1)
        }.toMap()
    }

    fun generateArray(depth: Int): List<Any?> {
        return (0 until arraySize).map {
            generateRandom(depth-1)
        }
    }


    enum class JsonType {
        OBJECT,
        ARRAY,
        STRING,
        NUMBER,
        BOOLEAN,
        NULL
    }

    val defaultFrequencies = mapOf(
        JsonType.OBJECT to 6,
        JsonType.ARRAY to 5,
        JsonType.STRING to 4,
        JsonType.NUMBER to 3,
        JsonType.BOOLEAN to 2,
        JsonType.NULL to 1,
    )

    val shallowFrequencies = defaultFrequencies.filter { (key, _) ->
        key != JsonType.OBJECT && key != JsonType.ARRAY
    }

    val random = Random(0)
}
