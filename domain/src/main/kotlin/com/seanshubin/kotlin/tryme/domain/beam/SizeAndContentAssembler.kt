package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.beam.AssemblerUtil.assembleList

class SizeAndContentAssembler(override val name:String):TreeAssembler {
    override fun assemble(assemblerMap: Map<String, TreeAssembler>, tree: Tree<Byte>): Any? {
        val list = assembleList(assemblerMap, tree)
        val size = list[0] as Int
        val content = list[1] as List<Byte>
        return SizeContent(size,content)
    }
}