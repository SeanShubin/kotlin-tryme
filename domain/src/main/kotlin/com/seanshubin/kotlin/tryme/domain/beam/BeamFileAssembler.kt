package com.seanshubin.kotlin.tryme.domain.beam

class BeamFileAssembler(override val name:String):TreeAssembler {
    override fun assemble(assemblerMap: Map<String, TreeAssembler>, tree: Tree<Byte>): Any? {
        tree as Tree.Branch
        val list = tree.list.map{
            val assemblerName = it.name
            val assembler = assemblerMap.getValue(assemblerName)
            assembler.assemble(assemblerMap, it)
        }
        val beamInterchangeFileFormat = list[0] as String
        val size = list[1] as UInt
        val beamSection = list[2] as String
        val beamFile = BeamFile(beamInterchangeFileFormat, size, beamSection)
        return beamFile
    }
}