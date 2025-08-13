package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToHex
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.ByteUtil.bytesToFloat

class ConstantPoolInfoFloat(
    override val index: Int,
    override val tag: ConstantPoolTag,
    val bytes: List<Byte>
) : ConstantPoolInfo {
    override fun line(): String {
        val bytesString = bytesToHex(bytes)
        val asFloat = bytesToFloat(bytes)
        return "[$index] ${tag.line()} bytes=$bytesString asFloat=$asFloat"
    }
}
