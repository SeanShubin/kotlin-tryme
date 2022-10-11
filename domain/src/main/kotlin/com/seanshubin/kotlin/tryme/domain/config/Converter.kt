package com.seanshubin.kotlin.tryme.domain.config

interface Converter<T> {
    val sourceType: Class<*>
    fun convert(value: Any?): T?
}
