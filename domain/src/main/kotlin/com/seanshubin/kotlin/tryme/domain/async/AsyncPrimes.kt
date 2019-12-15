package com.seanshubin.kotlin.tryme.domain.async

import com.seanshubin.kotlin.tryme.domain.format.DurationFormat
import com.seanshubin.kotlin.tryme.domain.timer.TimerFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.BigInteger

fun main(args: Array<String>) {
    val timer = TimerFactory.createDefault()
    val (duration, _) = timer.durationAndResult {
        val jobs = (1000..10000 step 1000).map {
            GlobalScope.launch {
                val longValue = it.toLong()
                val bigIntegerValue = BigInteger.valueOf(longValue)
                val prime = Prime.nthPrime(bigIntegerValue)
                println(prime)
            }
        }
        runBlocking {
            jobs.forEach { it.join() }
        }
    }
    val formattedTime = DurationFormat.milliseconds.format(duration.toMillis())
    println(formattedTime) //4 seconds 659 milliseconds
}
