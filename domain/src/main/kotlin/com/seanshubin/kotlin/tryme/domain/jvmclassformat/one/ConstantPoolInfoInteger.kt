package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToHex
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.ByteUtil.bytesToInt

class ConstantPoolInfoInteger(
    override val index: Int,
    override val tag: ConstantPoolTag,
    val bytes: List<Byte>
) : ConstantPoolInfo {
    override fun line(): String {
        val bytesString = bytesToHex(bytes)
        val asInt = bytesToInt(bytes)
        return "[$index] ${tag.line()} bytes=$bytesString asInt=$asInt"
    }
}
