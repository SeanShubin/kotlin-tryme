package com.seanshubin.kotlin.tryme.domain.dependency

import com.seanshubin.kotlin.tryme.domain.compare.ListComparator

interface Detail:Comparable<Detail> {
    val paths:List<ModulePath>
    val depth:Int
    val breadth:Int
    val transitive:Int
    fun toLines(context:List<String>):List<String>
    override fun compareTo(other: Detail): Int =
        compare.compare(this, other)
    companion object {
        val compareByDepth:Comparator<Detail> =
            Comparator<Detail> { o1, o2 -> o1.depth.compareTo(o2.depth) }.reversed()
        val compareByPaths:Comparator<Detail> =
            Comparator { o1, o2 -> ListComparator<ModulePath>().compare(o1.paths, o2.paths) }
        val compare:Comparator<Detail> = compareByDepth.thenComparing(compareByPaths)
    }
}
