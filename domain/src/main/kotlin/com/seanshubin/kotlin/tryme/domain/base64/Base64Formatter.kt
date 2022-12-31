package com.seanshubin.kotlin.tryme.domain.base64

import java.util.*

object Base64Formatter {
    fun format(b: ByteArray): String = Base64.getEncoder().encodeToString(b)
    fun parse(s: String): ByteArray = Base64.getDecoder().decode(s)
}