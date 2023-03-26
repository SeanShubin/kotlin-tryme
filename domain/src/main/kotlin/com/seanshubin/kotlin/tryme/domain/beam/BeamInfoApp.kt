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
            it.name to it
        }
        val atomSection = sectionsByName.getValue("AtU8")
        val importTableSection = sectionsByName.getValue("ImpT")
        val exportTableSection = sectionsByName.getValue("ExpT")
        val atoms = extractAtoms(atomSection.bytes)
        val importEntries = extractImportEntries(importTableSection.bytes)
        val imports = composeImports(atoms, importEntries)
        val exportEntries= extractExportEntries(exportTableSection.bytes)
        val exports = composeExports(atoms, exportEntries)
        println("atoms")
        atoms.forEachIndexed { index, atom ->
            println("${index+1} $atom")
        }
        println()

        println("import entries")
        importEntries.forEach(::println)
        println(importEntries.size)
        println()

        println("imports")
        imports.forEach(::println)
        println(imports.size)
        println()

        println("export entries")
        exportEntries.forEach(::println)
        println(exportEntries.size)
        println()

        println("exports")
        exports.forEach(::println)
        println(exports.size)
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

    fun extractExportEntries(bytes:List<Byte>):List<ExportEntry>{
        var remaining = bytes
        val count = bytes.take(4).toInt()
        remaining = remaining.drop(4)
        val exportEntries = mutableListOf<ExportEntry>()
        (0 until count).map {
            val function = remaining.take(4).toInt()
            remaining = remaining.drop(4)
            val arity = remaining.take(4).toInt()
            remaining = remaining.drop(4)
            val label = remaining.take(4).toInt()
            remaining = remaining.drop(4)
            exportEntries.add(ExportEntry(function, arity, label))
        }
        return exportEntries
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

    fun composeExports(atoms:List<String>,exportEntries:List<ExportEntry> ):List<Export>{
        val exports = exportEntries.map { exportEntry ->
            val function = atoms[exportEntry.function-1]
            val arity = exportEntry.arity
            val label = atoms[exportEntry.label-1]
            Export(function, arity, label)
        }
        return exports
    }
}

/*
00 00 00 04
00 00 00 20 00 00 00 01 00 00 00 11
00 00 00 20 00 00 00 00 00 00 00 0F
00 00 00 0F 00 00 00 00 00 00 00 0B
00 00 00 02 00 00 00 01 00 00 00 02                                     ....

 */