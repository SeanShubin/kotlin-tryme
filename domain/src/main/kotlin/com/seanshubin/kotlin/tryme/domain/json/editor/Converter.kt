package com.seanshubin.kotlin.tryme.domain.json.editor

import com.seanshubin.kotlin.tryme.domain.config.JsonFileConfiguration

interface Converter<T> {
    val sourceType: Class<*>
    fun convert(value: Any): T
    companion object {
        object StringConverter : Converter<String> {
            override val sourceType: Class<*> get() = String::class.java

            override fun convert(value: Any): String = value as String
        }

        object IntConverter : Converter<Int> {
            override val sourceType: Class<*> get() = Int::class.java

            override fun convert(value: Any): Int = value as Int
        }
    }
}
