package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.beam.Conversions.toInt
import com.seanshubin.kotlin.tryme.domain.cursor.IteratorCursor
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
        val cursor = IteratorCursor.create(bytes.iterator())
        val target = "beam-file"
        val parser = ParserImpl()
        val parseResult = parser.parse(target, cursor)
        val tree = when(parseResult){
            is Result.Success -> {
                parseResult.tree
            }
            is Result.Failure -> throw RuntimeException(parseResult.message)
        }
        val assembler = AssemblerImpl()
        val assembled = assembler.assemble(target, tree) as BeamFile
        val sectionsByName = assembled.sections.associate {
            it.name to it.bytes
        }
        val atomBytes = sectionsByName.getValue("AtU8")
        val importTableBytes = sectionsByName.getValue("ImpT")
        val atoms = extractAtoms(atomBytes)
        val importEntries = extractImportEntries(importTableBytes)
        println(atoms)
        println(atoms.size)
        println(importEntries)
        println(importEntries.size)
        val imports = composeImports(atoms, importEntries)
        imports.forEach(::println)
        println(imports.size)
    }

    fun displaySection(section:Section){
        println(section.name)
        println(section.size)
        section.bytes.toByteArray().toLines().forEach(::println)
    }

    fun extractAtoms(bytes:List<Byte>):List<String>{
        var remaining = bytes
        val count = bytes.take(4).toInt()
        remaining = remaining.drop(4)
        val atoms = mutableListOf<String>()
        (0 until count).map {
            val size = remaining.first().toInt()
            remaining = remaining.drop(1)
            val atom = String(remaining.take(size).toByteArray())
            remaining = remaining.drop(size)
            atoms.add(atom)
        }
        return atoms
    }

    fun extractImportEntries(bytes:List<Byte>):List<ImportEntry>{
        var remaining = bytes
        val count = bytes.take(4).toInt()
        remaining = remaining.drop(4)
        val importEntries = mutableListOf<ImportEntry>()
        (0 until count).map {
            val module = remaining.take(4).toInt()
            remaining = remaining.drop(4)
            val function = remaining.take(4).toInt()
            remaining = remaining.drop(4)
            val arity = remaining.take(4).toInt()
            remaining = remaining.drop(4)
            importEntries.add(ImportEntry(module, function, arity))
        }
        return importEntries
    }

    fun composeImports(atoms:List<String>,importEntries:List<ImportEntry> ):List<Import>{
        val imports = importEntries.map { importEntry ->
            val module = atoms[importEntry.module-1]
            val function = atoms[importEntry.function-1]
            val arity = importEntry.arity
            Import(module, function, arity)
        }
        return imports
    }
}
