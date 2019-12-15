package com.seanshubin.kotlin.tryme.domain.http

import com.seanshubin.kotlin.tryme.domain.string.StringUtil.toUtf8

data class Request(
    val uri: String,
    val method: String,
    val body: List<Byte>,
    val headers: List<Pair<String, String>>
) {
    fun toTable(): List<List<Any>> =
        listOf(
            listOf("uri", uri),
            listOf("method", method),
            listOf("body", body.toUtf8())
        ) + headersToTable()

    private fun headersToTable(): List<List<Any>> =
        headers.map { (name, value) -> listOf(name, value) }
}
