package com.seanshubin.kotlin.tryme.domain.compare

object ListUtil {
    fun <T> List<T>.splitByContiguousBlocks(vararg predicates: (T) -> Boolean): List<List<T>> {
        if (isEmpty() || predicates.isEmpty()) return listOf(this)

        val result = mutableListOf<List<T>>()
        var currentBlock = mutableListOf<T>()
        var currentIndex: Int? = null

        for (value in this) {
            val index = predicates.indexOfFirst { it(value) } // -1 if none match
            if (index != currentIndex && currentBlock.isNotEmpty()) {
                result.add(currentBlock)
                currentBlock = mutableListOf()
            }
            currentIndex = index
            currentBlock.add(value)
        }
        if (currentBlock.isNotEmpty()) result.add(currentBlock)
        return result
    }
}
