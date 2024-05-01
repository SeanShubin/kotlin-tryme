package com.seanshubin.kotlin.tryme.domain.huffman

interface HuffmanNode: Comparable<HuffmanNode> {
    val weight:Int
    val codeByValue:Map<String, String>
    fun toLines():List<String>
    fun valueCodeList():List<Pair<String, String>>
    fun encode(value:String):String = codeByValue.getValue(value)
    fun decode(encoded:String):String {
        var accumulator = ""
        var remain = encoded
        while(remain.isNotEmpty()){
            val result = decodeSingle(remain)
            accumulator += result.value
            remain = result.remain
        }
        return accumulator
    }
    fun decodeSingle(encoded:String):DecodeResult
    override fun compareTo(other: HuffmanNode): Int =
        this.weight.compareTo(other.weight)

    companion object{
        data class DecodeResult(val value:String, val remain: String)
        data class Tree(val left:HuffmanNode, val right:HuffmanNode):HuffmanNode {
            override val codeByValue: Map<String, String> = valueCodeList().toMap()
            override val weight: Int get() = left.weight + right.weight
            override fun toLines(): List<String> {
                val header = listOf(weight.toString())
                val remain = (left.toLines() + right.toLines()).map { "  $it"}
                return header + remain
            }

            override fun valueCodeList(): List<Pair<String, String>> {
                return left.valueCodeList().map(::prependZero) + right.valueCodeList().map(::prependOne)
            }

            private fun prepend(valueCode:Pair<String, String>, prefix:String):Pair<String, String> =
                valueCode.first to (prefix + valueCode.second)

            private fun prependZero(valueCode:Pair<String, String>):Pair<String, String> =
                prepend(valueCode, "0")
            private fun prependOne(valueCode:Pair<String, String>):Pair<String, String> =
                prepend(valueCode, "1")
            override fun decodeSingle(encoded: String): DecodeResult {
                val first = encoded[0]
                val remain = encoded.substring(1)
                val decodeResult = when(first){
                    '0' -> left.decodeSingle(remain)
                    '1' -> right.decodeSingle(remain)
                    else -> throw RuntimeException("expected 0 or 1, got '$first'")
                }
                return decodeResult
            }
        }
        data class Leaf(override val weight:Int, val value:String):HuffmanNode {
            override val codeByValue: Map<String, String> = valueCodeList().toMap()
            override fun toLines(): List<String> = listOf("$weight $value")
            override fun valueCodeList(): List<Pair<String, String>> =
                listOf(value to "")

            override fun decodeSingle(encoded: String): DecodeResult {
                return DecodeResult(value = value, remain = encoded)
            }
        }
        fun List<HuffmanNode>.buildHuffmanTree():HuffmanNode =
            sorted().buildHuffmanTreeAssumeSorted()

        private fun List<HuffmanNode>.buildHuffmanTreeAssumeSorted():HuffmanNode {
            if(size ==1) return get(0)
            val first = get(0)
            val second = get(1)
            val remain = drop(2)
            val newNode = Tree(first, second)
            val newList = remain + newNode
            return newList.buildHuffmanTree()
        }
    }
}