package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ByteUtil.bytesToLong
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToHex

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
