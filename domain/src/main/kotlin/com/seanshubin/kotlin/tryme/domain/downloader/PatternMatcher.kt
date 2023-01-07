package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI

class PatternMatcher(patterns: List<String>) : (URI) -> Boolean {
    private val regexList = patterns.map { pattern ->
        Regex(pattern)
    }

    override fun invoke(uri: URI): Boolean {
        return regexList.any { regex -> regex.matches(uri.path) }
    }
}
