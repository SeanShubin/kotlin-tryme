package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import com.seanshubin.kotlin.tryme.domain.json.JsonMappers
import java.io.DataInputStream
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

object ParseStandardLibraryApp {
    val outputDir = Paths.get("generated", "jmod-report")

    @JvmStatic
    fun main(args: Array<String>) {
        val inputDir = Paths.get("generated", "jmods")
        Files.walkFileTree(inputDir, ParseVisitor(inputDir))
    }

    fun shouldParse(filePath: Path): Boolean {
        val fileName = filePath.fileName.toString()
        return fileName.endsWith(".class") && !fileName.startsWith("module-info")
    }

    fun splitExt(path:Path): Pair<String, String> {
        val fileName = path.fileName.toString()
        val lastDotIndex = fileName.lastIndexOf('.')
        return if (lastDotIndex == -1) {
            Pair(fileName, "")
        } else {
            Pair(fileName.substring(0, lastDotIndex), fileName.substring(lastDotIndex + 1))
        }
    }

    fun parseFile(inputDir:Path, filePath:Path){
        if(!shouldParse(filePath)) {
            return
        }
        val relativePath = inputDir.relativize(filePath)
        val parent = relativePath.parent
        val (baseName, extension) = splitExt(relativePath.fileName)
        val outputBase = outputDir.resolve(parent)
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
        val json = JsonMappers.pretty.writeValueAsString(rawJvmClass)
        Files.writeString(rawPath, json)
        val jvmClass = JvmClass.fromRawJvmClass(rawJvmClass)
        val jvmClassObject = jvmClass.toObject()
        val jvmClassJson = JsonMappers.pretty.writeValueAsString(jvmClassObject)
        Files.writeString(interpretedPath, jvmClassJson)
        val methodDependencyLines = jvmClass.methodDependencies().flatMap{ (methodMakingCall, methodsBeingCalled) ->
            val methodsBeingCalledLines = methodsBeingCalled.map { methodBeingCalled ->
                "  $methodBeingCalled"
            }
            listOf(methodMakingCall) + methodsBeingCalledLines
        }
        methodDependencyLines.forEach(::println)
    }

    class ParseVisitor(private val inputDir:Path): FileVisitor<Path>{
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
            parseFile(inputDir,file)
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