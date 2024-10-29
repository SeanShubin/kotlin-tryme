package com.seanshubin.kotlin.tryme.domain.diskusagekotlin

import java.nio.file.Path

data class Entity(val sizeOfContents:Long, val fileCount:Int, val dirCount:Int, val path: Path) {
    companion object {
        val sizeAscending = Comparator<Entity> { o1, o2 -> o1.sizeOfContents.compareTo(o2.sizeOfContents) }
        val sizeDescending = sizeAscending.reversed()
    }
}