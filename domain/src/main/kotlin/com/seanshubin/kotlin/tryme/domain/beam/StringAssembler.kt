package com.seanshubin.kotlin.tryme.domain.beam

class StringAssembler(override val name:String):TreeAssembler {
    override fun assemble(assemblerMap: Map<String, TreeAssembler>, tree: Tree<Byte>): Any? {
        tree as Tree.Branch
        val byteList = tree.list.map{ leaf  ->
            leaf as Tree.Leaf
            leaf.value
        }
        return byteList.map {
            it.toInt().toChar()
        }.joinToString("")
    }
}