package com.seanshubin.kotlin.tryme.domain.crypto

import com.seanshubin.kotlin.tryme.domain.format.StringUtil.hex
import java.security.MessageDigest

class Md5Hash : OneWayHash {
    companion object {
        private val messageDigest: MessageDigest by lazy {
            MessageDigest.getInstance("MD5")
        }
    }

    override fun hash(s: String): String {
        val inputBytes = s.toByteArray()
        val hashedBytes = messageDigest.digest(inputBytes)
        val hashedString = hex(hashedBytes)
        return hashedString
    }
}
