package com.seanshubin.kotlin.tryme.domain.jvmclassformat

interface InterfaceEntry {
    fun toObject(): Map<String, Any>

    companion object {
        fun from(raw: UShort, constantPoolMap: Map<Int, ConstantPoolEntry>): InterfaceEntry {
            throw UnsupportedOperationException("not implemented")
        }
    }
}
