package com.seanshubin.kotlin.tryme.domain.jvmclassformat.util

import com.seanshubin.kotlin.tryme.domain.format.DurationFormat

class Profiler {
    val timings = mutableMapOf<String, Long>()
    val stack = mutableListOf<String>()
    fun <T> measure(caption:String, block: () -> T):T {
        stack.add(caption)
        val startTime = System.currentTimeMillis()
        val result = block()
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        addTiming(duration)
        stack.removeLast()
        return result
    }
    fun addTiming(duration: Long) {
        val address = stack.joinToString("/")
        timings[address] = (timings[address] ?: 0) + duration
    }
    fun lines():List<String>{
        val sortedTimings = timings.toList().sortedBy { it.second }.reversed()
        return sortedTimings.map { (caption, duration) ->
            "$caption: ${DurationFormat.milliseconds.format(duration)}"
        }
    }
}
