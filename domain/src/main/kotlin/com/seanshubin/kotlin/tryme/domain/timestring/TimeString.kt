package com.seanshubin.kotlin.tryme.domain.timestring

val timeStrings = listOf(
    "4:53",
    "4:52",
    "4:46",
    "5:58",
    "5:34",
    "6:23",
    "7:03",
    "4:51",
    "3:45",
    "4:39",
    "3:54",
    "3:42",
)

fun timeStringToSeconds(timeString:String):Int {
    val parts = timeString.split(":")
    val minutes = parts[0].toInt()
    val seconds = parts[1].toInt()
    return minutes * 60 + seconds
}

fun timeStringsToSeconds(timeStrings: List<String>):Int {
    val secondsList = timeStrings.map(::timeStringToSeconds)
    return secondsList.sum()
}

fun secondsToSummedString(totalSeconds:Int):String {
    val totalMinutes = totalSeconds / 60
    val minutes = totalMinutes % 60
    val hours = totalMinutes / 60
    val seconds = totalSeconds % 60
    val summedString = "$hours:$minutes:$seconds"
    return summedString
}

fun timeStringsToSummedString(timeStrings:List<String>):String {
    val seconds = timeStringsToSeconds(timeStrings)
    return secondsToSummedString(seconds)
}

fun main() {
    val summedString = timeStringsToSummedString(timeStrings)
    println(summedString)
}
