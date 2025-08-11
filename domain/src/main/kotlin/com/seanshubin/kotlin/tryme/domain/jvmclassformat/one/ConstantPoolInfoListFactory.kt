package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.Profiler
import java.io.DataInput

object ConstantPoolInfoListFactory {
    fun fromDataInput(input: DataInput, constantPoolCount: UShort, profiler: Profiler): List<ConstantPoolInfo> {
        val count = constantPoolCount.toInt()
        var index = 1
        val result = mutableListOf<ConstantPoolInfo>()
        while (index < count) {
            val constant = ConstantPoolInfoFactory.fromDataInput(input, index, profiler)
            index += constant.entriesTaken
            result.add(constant)
        }
        return result
    }
}