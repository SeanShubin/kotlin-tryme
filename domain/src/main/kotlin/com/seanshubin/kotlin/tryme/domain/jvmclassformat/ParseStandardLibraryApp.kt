package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import com.seanshubin.kotlin.tryme.domain.json.JsonMappers
import java.io.DataInputStream
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

object ParseStandardLibraryApp {
    val outputDir = Paths.get("generated", "jmod-report")

    @JvmStatic
    fun main(args: Array<String>) {
        val inputDir = Paths.get("generated", "jmods")
        var filesParsed = 0
        val parsingFileEvent: (Path) -> Unit = { filePath ->
            filesParsed++
            println("Parsing $filePath ($filesParsed)")
        }
        Files.walkFileTree(inputDir, ParseVisitor(inputDir, parsingFileEvent))
        println("Parsed $filesParsed files")
    }

    fun shouldParse(filePath: Path): Boolean {
        val fileName = filePath.fileName.toString()
        return fileName.endsWith(".class") && !fileName.startsWith("module-info")
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

    fun parseFile(inputDir: Path, filePath: Path, parsingFileEvent:(Path)->Unit) {
        if (!shouldParse(filePath)) {
            return
        }
        parsingFileEvent(filePath)
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
            val debugDataInput = DebugDataInput(dataInput, emit)
            RawJvmClass.fromDataInput(debugDataInput)
        }
        Files.write(dataPath, dataInputLines)
        val json = JsonMappers.pretty.writeValueAsString(rawJvmClass.toObject())
        Files.writeString(rawPath, json)
        val jvmClass = JvmClass.fromRawJvmClass(rawJvmClass)
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

    class ParseVisitor(private val inputDir: Path, private val parsingFileEvent:(path:Path)->Unit) : FileVisitor<Path> {
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
            parseFile(inputDir, file, parsingFileEvent)
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