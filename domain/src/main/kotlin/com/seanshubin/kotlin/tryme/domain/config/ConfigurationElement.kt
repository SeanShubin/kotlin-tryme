package com.seanshubin.kotlin.tryme.domain.config

interface ConfigurationElement<T> {
    val load:() -> T
    val store:(T)->Unit
}
