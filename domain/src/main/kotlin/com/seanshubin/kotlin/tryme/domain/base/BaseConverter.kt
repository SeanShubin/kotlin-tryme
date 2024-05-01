package com.seanshubin.kotlin.tryme.domain.base

import java.math.BigInteger

class BaseConverter(private val fromAlphabet:String, private val toAlphabet:String){
    fun convert(value:String):String = value.toBigInt(fromAlphabet).fromBigInt(toAlphabet)

    companion object {
        val binary = "01"
        val decimal = "0123456789"
        val hexUpper = "0123456789ABCDEF"
        val hexLower = "0123456789abcdef"
        val base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

        fun String.toBigInt(alphabet:String):BigInteger {
            var value = BigInteger.ZERO
            val alphabetLength = BigInteger.valueOf(alphabet.length.toLong())
            this.forEach {char ->
                val ordinal= alphabet.indexOf(char)
                if(ordinal < 0) throw RuntimeException("Char '$char' not found in alphabet '$alphabet'")
                value *= alphabetLength
                value += BigInteger.valueOf(ordinal.toLong())
            }
            return value
        }

        fun BigInteger.fromBigInt(alphabet:String):String {
            val buffer = StringBuffer()
            var value = this
            if(value == BigInteger.ZERO) return alphabet[0].toString()
            val alphabetLength = BigInteger.valueOf(alphabet.length.toLong())
            while(value != BigInteger.ZERO){
                val ordinal = value % alphabetLength
                val char = alphabet[ordinal.toInt()]
                buffer.append(char)
                value /= alphabetLength
            }
            return buffer.toString().reversed()
        }
    }
}
