package com.seanshubin.kotlin.tryme.domain.http

import com.seanshubin.kotlin.tryme.domain.collection.CollectionUtil.exactlyOne

data class Cookies(val cookies: List<Cookie>) {
    operator fun get(name: String): Cookie =
        cookies.filter { it.name.equals(name, ignoreCase = true) }.exactlyOne()
}