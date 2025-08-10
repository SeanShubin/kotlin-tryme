package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToLine

class ConstantPoolInfoDouble(
    override val index: Int,
    override val tag: ConstantPoolTag,
    val highBytes: List<Byte>,
    val lowBytes: List<Byte>
) : ConstantPoolInfo{
    override fun line(): String {
        return "[$index] ${tag.line()} highBytes=${bytesToLine(highBytes)} lowBytes=${bytesToLine(lowBytes)}"
    }
}
