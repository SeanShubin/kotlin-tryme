package com.seanshubin.kotlin.tryme.domain.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object JsonUtil {
    val pretty: ObjectMapper = ObjectMapper().registerKotlinModule().enable(SerializationFeature.INDENT_OUTPUT)
    val compact: ObjectMapper = ObjectMapper().registerKotlinModule()
    val parser: ObjectMapper = compact

    fun String.normalizeJson(): String {
        val asObject = parser.readValue<Any>(this)
        val asNormalized = pretty.writeValueAsString(asObject)
        return asNormalized
    }
}
