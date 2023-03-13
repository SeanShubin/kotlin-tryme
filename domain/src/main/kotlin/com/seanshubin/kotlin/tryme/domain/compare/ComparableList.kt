package com.seanshubin.kotlin.tryme.domain.compare

data class ComparableList<T:Comparable<T>>(val list:List<T>):Comparable<ComparableList<T>> {
    override fun compareTo(other: ComparableList<T>): Int =
        ListComparator<T>().compare(this.list, other.list)
}