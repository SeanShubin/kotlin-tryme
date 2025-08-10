package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import java.io.DataInput

object ConstantPoolInfoListFactory {
    fun fromDataInput(input: DataInput, constantPoolCount: UShort): List<ConstantPoolInfo> {
        var index = 1
        val result = mutableListOf<ConstantPoolInfo>()
        while (index < constantPoolCount.toInt()) {
            val constant = ConstantPoolInfoFactory.fromDataInput(input)
            index += constant.entriesTaken
            result.add(constant)
        }
        return result
    }
}