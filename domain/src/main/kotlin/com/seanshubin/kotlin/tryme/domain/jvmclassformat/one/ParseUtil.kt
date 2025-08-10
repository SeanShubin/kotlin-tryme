package com.seanshubin.kotlin.tryme.domain.jvmclassformat.one

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.DataInputEventsLines
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.LoggedDataInput
import java.io.DataInputStream
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.nio.file.attribute.BasicFileAttributes

object ParseUtil {
    fun parseFile(inputDir:Path, outputDir:Path, inputFile:Path) {
        val relativePath = inputDir.relativize(inputFile)
        val (fileNameWithoutExt, ext) = splitExt(relativePath)
        val dataFile = outputDir.resolve(fileNameWithoutExt + "-data.txt")
        Files.createDirectories(dataFile.parent)
        Files.deleteIfExists(dataFile)
        val structureFile = outputDir.resolve(fileNameWithoutExt + "-structure.txt")
        val emitToDataFile:(String)->Unit = { line ->
            Files.writeString(dataFile, line + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
        }
        val dataEvents = DataInputEventsLines(emitToDataFile)
        val jvmClassInfo = Files.newInputStream(inputFile).use { inputStream ->
            val loggedDataInput = LoggedDataInput(DataInputStream(inputStream), dataEvents)
            JvmClassInfo.fromDataInput(loggedDataInput)
        }
        Files.createDirectories(structureFile.parent)
        Files.write(structureFile, jvmClassInfo.lines(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
    }

    fun parseDir(inputDir: Path, outputDir:Path) {
        Files.walkFileTree(inputDir, ParseVisitor(inputDir, outputDir))
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

    class ParseVisitor(private val inputDir: Path, private val outputDir:Path) : FileVisitor<Path> {
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
            parseFile(inputDir, outputDir, file)
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