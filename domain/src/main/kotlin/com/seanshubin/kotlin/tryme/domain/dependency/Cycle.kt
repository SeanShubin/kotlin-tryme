package com.seanshubin.kotlin.tryme.domain.dependency

data class Cycle(
    val parts: List<ModulePath>
) {
    fun toObject():Map<String, Any> = mapOf(
        "parts" to parts.map{it.toObject()}
    )
}
