package com.seanshubin.kotlin.tryme.domain.compare

object ListUtil {
    fun <T> List<T>.splitByContiguousBlocks(predicates:List<(T)->Boolean>):List<List<T>>{
        if(isEmpty()) return listOf(this)
        if(predicates.isEmpty()) return listOf(this)
        val result = mutableListOf<List<T>>()
        var currentBlock = mutableListOf<T>()
        var currentPredicateIndex = -2
        var index =0
        while(index < size){
            val currentValue = get(index)
            val predicateIndex = predicates.indexOfFirst { it(currentValue) }
            if(predicateIndex != currentPredicateIndex){
                if(currentBlock.isNotEmpty()) {
                    result.add(currentBlock)
                    currentBlock = mutableListOf()
                }
                currentPredicateIndex = predicateIndex
            }
            currentBlock.add(currentValue)
            index++
        }
        if(currentBlock.isNotEmpty()){
            result.add(currentBlock)
        }
        return result
    }
}
