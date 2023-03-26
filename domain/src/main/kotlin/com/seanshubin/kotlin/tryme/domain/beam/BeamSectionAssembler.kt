package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.beam.AssemblerUtil.assembleList

class BeamSectionAssembler(override val name:String):TreeAssembler {
    override fun assemble(assemblerMap: Map<String, TreeAssembler>, tree: Tree<Byte>): Any? {
        val list = assembleList(assemblerMap, tree)
        val name = list[0] as String
        val sizeContent = list[1] as SizeContent
        return Section(name, sizeContent.size, sizeContent.content)
    }
}