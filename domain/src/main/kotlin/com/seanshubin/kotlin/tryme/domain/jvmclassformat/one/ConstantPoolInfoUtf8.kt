package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToHex
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToSanitizedString

class ConstantPoolInfoUtf8(
    override val index: Int,
    override val tag: ConstantPoolTag,
    val length: UShort,
    val bytes: List<Byte>
) : ConstantPoolInfo {
    override fun line(): String {
        val asHex = bytesToHex(bytes)
        val asString = bytesToSanitizedString(bytes)
        return "[$index] ${tag.line()} length=$length bytes=$asHex asString=$asString"
    }
}
