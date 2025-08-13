package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToHex
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.ByteUtil.bytesToLong

class ConstantPoolInfoLong(
    override val index: Int,
    override val tag: ConstantPoolTag,
    val highBytes: List<Byte>,
    val lowBytes: List<Byte>
) : ConstantPoolInfo {
    override fun line(): String {
        val highBytesString = bytesToHex(highBytes)
        val lowBytesString = bytesToHex(lowBytes)
        val asLong = bytesToLong(highBytes + lowBytes)
        return "[$index] ${tag.line()} highBytes=$highBytesString lowBytes=$lowBytesString asLong=$asLong"
    }
}
