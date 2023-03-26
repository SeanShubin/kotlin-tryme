package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.beam.AssemblerUtil.assembleList

class BeamSectionListAssembler(override val name:String):TreeAssembler {
    override fun assemble(assemblerMap: Map<String, TreeAssembler>, tree: Tree<Byte>): Any? {
        val list = assembleList(assemblerMap, tree)
        val sections:List<Section> = list.map {
            it as Section
        }
        return sections
    }
}