package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import com.seanshubin.kotlin.tryme.domain.format.DurationFormat
import com.seanshubin.kotlin.tryme.domain.json.JsonMappers
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.Profiler
import java.io.DataInputStream
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

object ParseStandardLibraryApp {
    val outputDir = Paths.get("generated", "jmod-report")

    @JvmStatic
    fun main(args: Array<String>) {
        val profiler = Profiler()
        val startTime = System.currentTimeMillis()
        val inputDir = Paths.get("generated", "jmods", "java.base.jmod")
        val summaryFile  = outputDir.resolve("summary.txt")
        val profilerFile = outputDir.resolve("profiler.txt")
        Files.deleteIfExists(summaryFile)
        Files.deleteIfExists(profilerFile)
        fun emitSummary(line: String) {
            Files.write(summaryFile, listOf(line), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
            println(line)
        }
        var filesParsed = 0
        val codeHistogram = mutableMapOf<Code, Int>()
        Code.entries.forEach { code ->
            codeHistogram[code] = 0
        }
        var totalCodes = 0
        val events = object : Events {
            override fun opCodeParsed(opCode: OpCodeEntry) {
                codeHistogram.merge(opCode.code, 1) { oldValue, newValue -> oldValue + newValue }
                totalCodes++
            }

            override fun parsingFile(path: Path) {
                filesParsed++
                println("Parsing $path ($filesParsed)")
            }
        }
        Files.walkFileTree(inputDir, ParseVisitor(inputDir, events, profiler))
        emitSummary("Parsed $filesParsed files")
        emitSummary("Total codes: $totalCodes")
        emitSummary("Code histogram:")
        Files.write(profilerFile, profiler.lines(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
        codeHistogram.entries.sortedByDescending { it.value }.forEach { (code, count) ->
            println("  $code: $count")
        }
        val endTime = System.currentTimeMillis()
        val durationMillis = endTime - startTime
        DurationFormat.milliseconds.format(durationMillis).let {
            emitSummary("Time taken: $it")
        }
    }

    fun shouldParse(filePath: Path): Boolean {
        val fileName = filePath.fileName.toString()
        return fileName.endsWith(".class")
    }

    fun splitExt(path: Path): Pair<String, String> {
        val fileName = path.fileName.toString()
        val lastDotIndex = fileName.lastIndexOf('.')
        return if (lastDotIndex == -1) {
            Pair(fileName, "")
        } else {
            Pair(fileName.substring(0, lastDotIndex), fileName.substring(lastDotIndex + 1))
        }
    }

    fun parseFile(inputDir: Path, filePath: Path, events:Events, profiler:Profiler) {
        if (!shouldParse(filePath)) {
            return
        }
        events.parsingFile(filePath)
        val relativePath = inputDir.relativize(filePath)
        val parent = relativePath.parent
        val (baseName, extension) = splitExt(relativePath.fileName)
        val outputBase = outputDir.resolve(parent)
        val methodDependencies = outputDir.resolve("method-dependencies.txt")
        Files.createDirectories(outputBase)
        val dataPath = outputBase.resolve("$baseName-data.txt")
        val rawPath = outputBase.resolve("$baseName-raw.json")
        val interpretedPath = outputBase.resolve("$baseName-interpreted.json")
        val dataInputLines = mutableListOf<String>()
        val emit: (String) -> Unit = { dataInputLines.add(it) }
        val rawJvmClass = Files.newInputStream(filePath).use { inputStream ->
            val dataInput = DataInputStream(inputStream)
            val debugDataInput = DebugDataInput(dataInput, emit, profiler)
            RawJvmClass.fromDataInput(debugDataInput)
        }
        Files.write(dataPath, dataInputLines)
        val json = JsonMappers.pretty.writeValueAsString(rawJvmClass.toObject())
        Files.writeString(rawPath, json)
        val jvmClass = JvmClass.fromRawJvmClass(rawJvmClass, events)
        val jvmClassObject = jvmClass.toObject()
        val jvmClassJson = JsonMappers.pretty.writeValueAsString(jvmClassObject)
        Files.writeString(interpretedPath, jvmClassJson)
        val methodDependencyLines = jvmClass.methodDependencies().flatMap { (methodMakingCall, methodsBeingCalled) ->
            val methodsBeingCalledLines = methodsBeingCalled.map { methodBeingCalled ->
                "  $methodBeingCalled"
            }
            listOf(methodMakingCall) + methodsBeingCalledLines
        }
        Files.write(methodDependencies, methodDependencyLines, StandardOpenOption.APPEND, StandardOpenOption.CREATE)
        methodDependencyLines.forEach(::println)
    }

    class ParseVisitor(
        private val inputDir: Path,
        private val events:Events,
        private val profiler:Profiler) : FileVisitor<Path> {
        override fun preVisitDirectory(
            dir: Path,
            attrs: BasicFileAttributes
        ): FileVisitResult {
            return FileVisitResult.CONTINUE
        }

        override fun visitFile(
            file: Path,
            attrs: BasicFileAttributes
        ): FileVisitResult {
            parseFile(inputDir, file, events, profiler)
            return FileVisitResult.CONTINUE
        }

        override fun visitFileFailed(
            file: Path?,
            exc: IOException
        ): FileVisitResult {
            throw UnsupportedOperationException("Not Implemented!")
        }

        override fun postVisitDirectory(
            dir: Path?,
            exc: IOException?
        ): FileVisitResult {
            return FileVisitResult.CONTINUE
        }
    }
}
