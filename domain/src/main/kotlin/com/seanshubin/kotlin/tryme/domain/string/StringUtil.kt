package com.seanshubin.kotlin.tryme.domain.string

import java.nio.charset.StandardCharsets

object StringUtil {
    fun List<Byte>.toUtf8() = this.toByteArray().toString(StandardCharsets.UTF_8)
}
