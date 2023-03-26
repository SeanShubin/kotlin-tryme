package com.seanshubin.kotlin.tryme.domain.beam

interface Assembler {
    fun assemble(name:String, tree: Tree<Byte>): Any?
}
