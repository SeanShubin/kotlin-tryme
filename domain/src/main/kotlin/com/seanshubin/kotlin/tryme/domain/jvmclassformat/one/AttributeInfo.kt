package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToDisplay
import java.io.DataInput

data class AttributeInfo(
    val attributeNameIndex: UShort,
    val attributeLength: Int,
    val info: List<Byte>
) {
    fun line():String{
        return "attributeNameIndex=$attributeNameIndex attributeLength=$attributeLength info=${bytesToDisplay(info)}"
    }
    companion object {
        fun fromDataInput(input: DataInput): AttributeInfo {
            val attributeNameIndex = input.readUnsignedShort().toUShort()
            val attributeLength = input.readInt()
            val info = ByteArray(attributeLength)
            input.readFully(info)
            return AttributeInfo(attributeNameIndex, attributeLength, info.toList())
        }
    }
}
