package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.cursor.Cursor
import com.seanshubin.kotlin.tryme.domain.cursor.IteratorCursor
import com.seanshubin.kotlin.tryme.domain.io.ioutil.toLineIterator
import com.seanshubin.kotlin.tryme.domain.string.ByteArrayFormatHex
import com.seanshubin.kotlin.tryme.domain.string.ByteArrayFormatHex.toLines
import java.nio.file.Files
import java.nio.file.Paths

// https://beam-wisdoms.clau.se/en/latest/indepth-beam-file.html
object BeamInfoApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val file = Paths.get(args[0])
        val inputStream = Files.newInputStream(file)
        val bytes = inputStream.readAllBytes()
        bytes.toLines().forEach(::println)
        val cursor = IteratorCursor.create(bytes.iterator())
        val target = "beam-file"
        val parser = ParserImpl()
        val parseResult = parser.parse(target, cursor)
        val tree = when(parseResult){
            is Result.Success -> {
                parseResult.tree.toLines().forEach(::println)
                parseResult.tree
            }
            is Result.Failure -> throw RuntimeException(parseResult.message)
        }
        val assembler = AssemblerImpl()
        val assembled = assembler.assemble(target, tree)
        println(assembled)

    }
}
