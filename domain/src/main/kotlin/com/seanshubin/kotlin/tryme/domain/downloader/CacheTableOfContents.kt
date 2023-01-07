package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI

data class CacheTableOfContents(val entries:Map<URI, CacheEntry>){
    fun addEntry(entry:CacheEntry):CacheTableOfContents{
        val newEntries = entries + Pair(entry.uri, entry)
        return copy(entries = newEntries)
    }
    companion object {
        val empty = CacheTableOfContents(entries = emptyMap())
    }
}