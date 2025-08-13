package com.seanshubin.kotlin.tryme.domain.jvmclassformat.util

import com.seanshubin.kotlin.tryme.domain.format.DurationFormat

class Profiler {
    val timings = mutableMapOf<String, Long>()
    val stack = ArrayDeque<String>()
    val quantities = mutableMapOf<String, Int>()
    fun <T> measure(caption:String, block: () -> T):T {
        return updateStack(caption){
            val startTime = System.currentTimeMillis()
            val result = block()
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime
            addTiming(duration)
            result
        }
    }
    fun <T> updateStack(caption:String, block:()->T):T {
        stack.add(caption)
        val result = try {
            block()
        } finally {
            stack.removeLast()
        }
        return result
    }
    fun addTiming(duration: Long) {
        val address = stack.joinToString("/")
        timings[address] = (timings[address] ?: 0) + duration
        quantities[address] = (quantities[address] ?: 0) + 1
    }
    fun lines():List<String>{
        val sortedTimings = timings.toList().sortedBy { it.second }.reversed()
        return sortedTimings.map { (caption, duration) ->
            val quantity = quantities.getValue(caption)
            val quantityString = String.format("%,d", quantity)
            "$caption($quantityString): ${DurationFormat.milliseconds.format(duration)}"
        }
    }
}
