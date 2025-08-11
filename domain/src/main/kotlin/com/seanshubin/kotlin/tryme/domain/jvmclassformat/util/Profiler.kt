package com.seanshubin.kotlin.tryme.domain.jvmclassformat.util

import com.seanshubin.kotlin.tryme.domain.format.DurationFormat

class Profiler {
    val timings:MutableMap<String, Long> = mutableMapOf()
    fun <T> measure(caption:String, block: () -> T):T {
        val startTime = System.currentTimeMillis()
        val result = block()
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        addTiming(caption, duration)
        return result
    }
    fun addTiming(caption: String, duration: Long) {
        timings[caption] = (timings[caption] ?: 0) + duration
    }
    fun lines():List<String>{
        val sortedTimings = timings.toList().sortedBy { it.second }.reversed()
        return sortedTimings.map { (caption, duration) ->
            "$caption: ${DurationFormat.milliseconds.format(duration)}"
        }
    }
}
