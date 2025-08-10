package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ByteUtil.bytesToDouble
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToHex

class ConstantPoolInfoDouble(
    override val index: Int,
    override val tag: ConstantPoolTag,
    val highBytes: List<Byte>,
    val lowBytes: List<Byte>
) : ConstantPoolInfo {
    override fun line(): String {
        val highBytesString = bytesToHex(highBytes)
        val lowBytesString = bytesToHex(lowBytes)
        val asDouble = bytesToDouble(highBytes + lowBytes)
        return "[$index] ${tag.line()} highBytes=$highBytesString lowBytes=$lowBytesString asDouble=$asDouble"
    }
}
