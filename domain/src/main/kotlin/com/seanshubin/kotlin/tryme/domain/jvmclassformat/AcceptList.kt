package com.seanshubin.kotlin.tryme.domain.jvmclassformat

class AcceptList(val patterns:List<Regex>): (String)->Boolean {
    override fun invoke(input: String): Boolean {
        return patterns.any { it.matches(input) }
    }

    companion object {
        fun fromPatterns(patterns: List<String>): AcceptList {
            val regexes = patterns.map { pattern ->
                Regex(pattern)
            }
            return AcceptList(regexes)
        }
    }
}
