package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.beam.AssemblerUtil.assembleList

class BeamFileAssembler(override val name:String):TreeAssembler {
    override fun assemble(assemblerMap: Map<String, TreeAssembler>, tree: Tree<Byte>): Any? {
        val list = assembleList(assemblerMap, tree)
        val beamInterchangeFileFormat = list[0] as String
        val size = list[1] as Int
        val beamSectionLiteral = list[2] as String
        val beamSections = list[3] as List<Section>
        val beamFile = BeamFile(beamInterchangeFileFormat, size, beamSectionLiteral, beamSections)
        return beamFile
    }
}
