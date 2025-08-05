package com.seanshubin.kotlin.tryme.domain.jvmclassformat

data class MatchOffset(val match: Int, val offset: Int) {
    fun toObject(): Map<String, Any> {
        return mapOf(
            "match" to match,
            "offset" to offset
        )
    }
}
