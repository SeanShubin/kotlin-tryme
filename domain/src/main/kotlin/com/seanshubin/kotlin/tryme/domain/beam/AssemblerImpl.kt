package com.seanshubin.kotlin.tryme.domain.beam

class AssemblerImpl:Assembler {
    override fun assemble(name: String, tree: Tree<Byte>): Any? {
        val map =AssemblerRepository.map
        return map.getValue(name).assemble(map, tree)
    }
}
