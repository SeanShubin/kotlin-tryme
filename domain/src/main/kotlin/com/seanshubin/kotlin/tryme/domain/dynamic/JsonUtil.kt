package com.seanshubin.kotlin.tryme.domain.dynamic

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.kotlin.tryme.domain.json.JsonMappers

object JsonUtil {
    fun parseCsvLine(line: String): List<String> {
        return LineParseState.create(line).navigateToEnd().cells
    }

    fun coerceStrings(o: Any?): Any? =
        when (o) {
            is Map<*, *> -> o.map { (key, value) ->
                key to coerceStrings(value)
            }.toMap()

            is List<*> ->
                o.map { element -> coerceStrings(element) }

            is String -> try {
                coerceStrings(JsonMappers.parser.readValue<Any?>(o))
            } catch (e: JsonParseException) {
                o
            } catch (e: MismatchedInputException) {
                o
            }

            else -> o
        }
}

