package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI

data class PatternMatcher(override val name:String, val patterns: List<String>) : UriPolicy {
    private val regexList = patterns.map { pattern ->
        Regex(pattern)
    }

    override fun accept(uri: URI): Boolean {
        return regexList.any { regex -> regex.matches(uri.path) }
    }
}
