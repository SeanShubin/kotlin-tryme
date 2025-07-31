package com.seanshubin.kotlin.tryme.domain.jvmclassformat

class ConstantPoolLookupImpl(private val constantPool: List<ConstantPoolInfo>) : ConstantPoolLookup {
    private val byIndex: Map<Int, ConstantPoolInfo>

    init {
        val mutableByIndex = mutableMapOf<Int, ConstantPoolInfo>()
        var currentIndex = 1
        for (constantPoolInfo in constantPool) {
            mutableByIndex[currentIndex] = constantPoolInfo
            currentIndex += when (constantPoolInfo) {
                is ConstantPoolInfo.ConstantLong, is ConstantPoolInfo.ConstantDouble -> 2
                else -> 1
            }
        }
        byIndex = mutableByIndex
    }

    override fun lookup(index: UShort): ConstantPoolInfo {
        return byIndex.getValue(index.toInt())
    }
}