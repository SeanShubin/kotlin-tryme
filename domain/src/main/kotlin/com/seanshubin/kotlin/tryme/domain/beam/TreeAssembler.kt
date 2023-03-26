package com.seanshubin.kotlin.tryme.domain.beam

interface TreeAssembler {
    val name:String
    fun assemble(assemblerMap: Map<String, TreeAssembler>, tree: Tree<Byte>): Any?
}
