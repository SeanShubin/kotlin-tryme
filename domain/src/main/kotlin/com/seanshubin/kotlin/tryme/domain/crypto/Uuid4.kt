package com.seanshubin.kotlin.tryme.domain.crypto

import java.util.*

class Uuid4 : UniqueIdGenerator {
    override fun uniqueId(): String {
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }
}
