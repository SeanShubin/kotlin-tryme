package com.seanshubin.kotlin.tryme.domain.dynamic

class LookupByFrequency<T> (frequencies: Map<T, Int>) {
    val thresholds = frequencies.toList().fold(emptyList(), ::addFrequency)
    fun lookup(index:Int):T {
       thresholds.forEach{ (value, threshold) ->
           if(index < threshold) return value
       }
        throw RuntimeException("Index can not be higher than ${thresholds.last().second}")
    }
    private fun addFrequency(soFar:List<Pair<T, Int>>, next:Pair<T, Int>):List<Pair<T, Int>> {
        val oldTotal = if(soFar.isEmpty()) 0 else soFar.last().second
        val newTotal = oldTotal + next.second
        return soFar + Pair(next.first, newTotal)
    }
}