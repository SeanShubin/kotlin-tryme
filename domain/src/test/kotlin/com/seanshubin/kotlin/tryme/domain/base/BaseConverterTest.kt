package com.seanshubin.kotlin.tryme.domain.base

import kotlin.test.Test
import kotlin.test.assertEquals

class BaseConverterTest {
    @Test
    fun prototype(){
        val baseConverter = BaseConverter(BaseConverter.base64, BaseConverter.decimal)
        println(baseConverter.convert(BaseConverter.base64.reversed()))
    }
    @Test
    fun baseTenToBinary(){
        val baseConverter = BaseConverter(BaseConverter.decimal, BaseConverter.binary)
        assertConvert(baseConverter, "0", "0")
        assertConvert(baseConverter, "1", "1")
        assertConvert(baseConverter, "2", "10")
        assertConvert(baseConverter, "3", "11")
        assertConvert(baseConverter, "4", "100")
        assertConvert(baseConverter, "5", "101")
        assertConvert(baseConverter, "6", "110")
        assertConvert(baseConverter, "7", "111")
        assertConvert(baseConverter, "8", "1000")
        assertConvert(baseConverter, "15", "1111")
        assertConvert(baseConverter, "16", "10000")
        assertConvert(baseConverter, "255", "11111111")
        assertConvert(baseConverter, "256", "100000000")
        assertConvert(baseConverter, "65535", "1111111111111111")
        assertConvert(baseConverter, "65536", "10000000000000000")
    }

    @Test
    fun baseTenToHexUpper(){
        val baseConverter = BaseConverter(BaseConverter.decimal, BaseConverter.hexUpper)
        assertConvert(baseConverter, "0", "0")
        assertConvert(baseConverter, "1", "1")
        assertConvert(baseConverter, "2", "2")
        assertConvert(baseConverter, "3", "3")
        assertConvert(baseConverter, "4", "4")
        assertConvert(baseConverter, "5", "5")
        assertConvert(baseConverter, "6", "6")
        assertConvert(baseConverter, "7", "7")
        assertConvert(baseConverter, "8", "8")
        assertConvert(baseConverter, "15", "F")
        assertConvert(baseConverter, "16", "10")
        assertConvert(baseConverter, "255", "FF")
        assertConvert(baseConverter, "256", "100")
        assertConvert(baseConverter, "65535", "FFFF")
        assertConvert(baseConverter, "65536", "10000")
        assertConvert(baseConverter, "18364758544493064720", "FEDCBA9876543210")
    }

    @Test
    fun baseTenToHexLower(){
        val baseConverter = BaseConverter(BaseConverter.decimal, BaseConverter.hexLower)
        assertConvert(baseConverter, "0", "0")
        assertConvert(baseConverter, "1", "1")
        assertConvert(baseConverter, "2", "2")
        assertConvert(baseConverter, "3", "3")
        assertConvert(baseConverter, "4", "4")
        assertConvert(baseConverter, "5", "5")
        assertConvert(baseConverter, "6", "6")
        assertConvert(baseConverter, "7", "7")
        assertConvert(baseConverter, "8", "8")
        assertConvert(baseConverter, "15", "f")
        assertConvert(baseConverter, "16", "10")
        assertConvert(baseConverter, "255", "ff")
        assertConvert(baseConverter, "256", "100")
        assertConvert(baseConverter, "65535", "ffff")
        assertConvert(baseConverter, "65536", "10000")
        assertConvert(baseConverter, "18364758544493064720", "fedcba9876543210")
    }

    @Test
    fun baseTenToBase64(){
        val baseConverter = BaseConverter(BaseConverter.decimal, BaseConverter.base64)
        assertConvert(baseConverter, "0", "A")
        assertConvert(baseConverter, "15", "P")
        assertConvert(baseConverter, "16", "Q")
        assertConvert(baseConverter, "255", "D/")
        assertConvert(baseConverter, "256", "EA")
        assertConvert(baseConverter, "65535", "P//")
        assertConvert(baseConverter, "65536", "QAA")
        assertConvert(baseConverter, "39392078757191557952714343944915560488424390381760365930566598193007564623154681709773698286566728461254900444831808", "/+9876543210zyxwvutsrqponmlkjihgfedcbaZYXWVUTSRQPONMLKJIHGFEDCBA")
    }

    private fun assertConvert(baseConverter:BaseConverter, input:String, expected:String){
        val actual = baseConverter.convert(input)
        assertEquals(expected, actual)
    }
}