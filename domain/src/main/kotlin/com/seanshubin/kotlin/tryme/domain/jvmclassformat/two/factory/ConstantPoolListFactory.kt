package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolTag
import java.io.DataInput

object ConstantPoolListFactory {
    private val constantPoolFactoryMap:Map<ConstantPoolTag, ConstantPoolFactory> = mapOf(
        ConstantPoolTag.Utf8 to ConstantPoolFactoryUtf8
    )
    fun allConstants(dataInput: DataInput): List<ConstantPoolApi> {
        val constantPoolCount = dataInput.readUnsignedShort().toUShort()
        var index = 1u
        val constants = mutableListOf<ConstantPoolApi>()
        while(index < constantPoolCount){
            val constant = singleConstant(dataInput, index.toUShort())
            constants.add(constant)
            index += constant.entriesConsumed()
        }
        return constants
    }

    fun singleConstant(dataInput:DataInput, index:UShort): ConstantPoolApi {
        val tagValue = dataInput.readUnsignedByte().toUByte()
        val tag = ConstantPoolTag.fromValue(tagValue)
        val result = constantPoolFactoryMap.getValue(tag).fromDataInput(dataInput)
        return result
    }

    data class UntypedConstant(val tag:ConstantPoolTag, val bytes: List<Byte>)
}
