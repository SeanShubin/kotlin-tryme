package com.seanshubin.kotlin.tryme.domain.config

import java.nio.file.Path
import java.time.Instant

interface Configuration {
    fun intAt(default: Any?, keys: List<String>): ConfigurationElement<Int>
    fun stringAt(default: Any?, keys: List<String>): ConfigurationElement<String>
    fun pathAt(default: Any?, keys: List<String>): ConfigurationElement<Path>
    fun instantAt(default: Any?, keys: List<String>): ConfigurationElement<Instant>
    fun formattedSecondsAt(default: Any?, keys: List<String>): ConfigurationElement<Long>
    fun stringListAt(default: Any?, keys: List<String>):ConfigurationElement<List<String>>
    fun pathListAt(default: Any?, keys: List<String>): ConfigurationElement<List<Path>>
}
