package com.seanshubin.kotlin.tryme.domain.dependency

import com.seanshubin.kotlin.tryme.domain.compare.ListComparator

data class ModulePath(val pathParts: List<String>):Comparable<ModulePath> {
    constructor(vararg pathPartsArray: String) : this(pathPartsArray.toList())

    val simpleName get() = formatName(defaultNameFormat)
    fun linkName(makeLink:(ModulePath)->String?):String {
        val link = makeLink(this)
        return if(link == null){
            simpleName
        } else {
            link
        }
    }
    fun toObject(): List<String> = pathParts
    fun formatName(format: (List<String>) -> String): String = format(pathParts)

    override fun compareTo(other: ModulePath): Int =
        ListComparator<String>().compare(pathParts, other.pathParts)

    companion object {
        val defaultNameFormat: (List<String>) -> String = {
            when (it.size) {
                0 -> {
                    "\"--root--\""
                }
                1 -> {
                    it[0]
                }
                else -> {
                    it.joinToString(".", "\"", "\"")
                }
            }
        }
    }
}
