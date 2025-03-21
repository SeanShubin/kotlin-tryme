package com.seanshubin.kotlin.tryme.domain.dynamic

import com.fasterxml.jackson.core.JsonParseException

sealed interface JsonParseResult {
    data class Success(val value:Any?) : JsonParseResult
    data class Failure(val ex: JsonParseException) : JsonParseResult
}
