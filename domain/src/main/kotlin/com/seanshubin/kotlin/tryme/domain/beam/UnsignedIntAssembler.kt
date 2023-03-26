package com.seanshubin.kotlin.tryme.domain.beam

import java.nio.ByteBuffer

/*
int to byte
    byte[] result =  ByteBuffer.allocate(4).putInt(number).array();
byte to int
    int num = ByteBuffer.wrap(bytes).getInt();
 */
class UnsignedIntAssembler(override val name:String):TreeAssembler {
    override fun assemble(assemblerMap: Map<String, TreeAssembler>, tree: Tree<Byte>): Any? {
        tree as Tree.Branch
        val byteList = tree.list.map{ leaf  ->
            leaf as Tree.Leaf
            leaf.value
        }
        val byteArray = byteList.toByteArray()
        val unsignedInt:UInt = ByteBuffer.wrap(byteArray).int.toUInt()
        return unsignedInt
    }
}
