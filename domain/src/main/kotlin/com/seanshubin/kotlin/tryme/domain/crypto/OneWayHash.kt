package com.seanshubin.kotlin.tryme.domain.crypto

interface OneWayHash {
    fun hash(s: String): String
}
