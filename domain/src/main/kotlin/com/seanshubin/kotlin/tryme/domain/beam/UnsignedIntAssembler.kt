package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.beam.AssemblerUtil.assembleByteList
import com.seanshubin.kotlin.tryme.domain.beam.Conversions.toInt

class UnsignedIntAssembler(override val name:String):TreeAssembler {
    override fun assemble(assemblerMap: Map<String, TreeAssembler>, tree: Tree<Byte>): Any? {
        val byteList = assembleByteList(tree)
        val unsignedInt:Int = byteList.toInt()
        return unsignedInt
    }
}
