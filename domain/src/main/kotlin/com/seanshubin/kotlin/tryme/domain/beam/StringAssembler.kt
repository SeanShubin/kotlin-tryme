package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.beam.AssemblerUtil.assembleByteList

class StringAssembler(override val name:String):TreeAssembler {
    override fun assemble(assemblerMap: Map<String, TreeAssembler>, tree: Tree<Byte>): Any? {
        val byteList = assembleByteList(tree)
        return byteList.map {
            it.toInt().toChar()
        }.joinToString("")
    }
}
