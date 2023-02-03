package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI

data class PatternMatcher(override val name:String, val includePatternList: List<String>, val excludePatternList:List<String>) : UriPolicy {
    private val includeRegexList = includePatternList.map { pattern ->
        Regex(pattern)
    }
    private val excludeRegexList = excludePatternList.map { pattern ->
        Regex(pattern)
    }

    override fun accept(uri: URI): Boolean {
        val matchesInclude = includeRegexList.any { regex -> regex.matches(uri.path)}
        if(!matchesInclude) return false
        if(excludeRegexList.isEmpty()) return true
        val matchesExclude = excludeRegexList.any {regex -> regex.matches(uri.path) }
        return !matchesExclude
    }
}
