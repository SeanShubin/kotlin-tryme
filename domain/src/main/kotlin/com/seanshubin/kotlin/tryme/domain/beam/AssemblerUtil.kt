package com.seanshubin.kotlin.tryme.domain.beam

object AssemblerUtil {
    fun assembleList(assemblerMap: Map<String, TreeAssembler>, tree: Tree<Byte>): List<Any?> {
        tree as Tree.Branch
        val list = tree.list.map{
            val assemblerName = it.name
            val assembler = assemblerMap.getValue(assemblerName)
            assembler.assemble(assemblerMap, it)
        }
        return list
    }

    fun assembleByteList(tree:Tree<Byte>):List<Byte> {
        tree as Tree.Branch
        val byteList = tree.list.map { leaf ->
            leaf as Tree.Leaf
            leaf.value
        }
        return byteList
    }
}
