package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import com.seanshubin.kotlin.tryme.domain.format.DurationFormat
import com.seanshubin.kotlin.tryme.domain.json.JsonMappers
import java.io.DataInputStream
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.function.Predicate

object ParseSampleApp {
    val outputDir = Paths.get("generated", "sample-app")
    val resourceDir = Paths.get("domain", "src", "main", "resources")

    @JvmStatic
    fun main(args: Array<String>) {
        val startTime = System.currentTimeMillis()

        val inputDir = Paths.get(
            "domain",
            "target",
            "classes",
            "com",
            "seanshubin",
            "kotlin",
            "tryme",
            "domain",
            "jvmclassformat",
            "samples"
        )
        var filesParsed = 0
        val events = object : Events {
            override fun opCodeParsed(opCode: OpCodeEntry) {
            }

            override fun parsingFile(path: Path) {
                filesParsed++
                println("Parsing $path")
            }
        }
        val isOnBlacklist = loadRegexPredicate(
           resourceDir.resolve( "blacklist.txt")
        )
        val isOnWhitelist = loadRegexPredicate(resourceDir.resolve("whitelist.txt"))
        Files.walkFileTree(inputDir, ParseVisitor(inputDir,isOnBlacklist, isOnWhitelist, events))
        println("Parsed $filesParsed files")
        val endTime = System.currentTimeMillis()
        val durationMillis = endTime - startTime
        DurationFormat.milliseconds.format(durationMillis).let {
            println(it)
        }
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

    fun parseFile(inputDir: Path, filePath: Path, isOnBlackList:(String)->Boolean, isOnWhiteList:(String)->Boolean, events: Events) {
        if (!shouldParse(filePath)) {
            return
        }
        events.parsingFile(filePath)
        val relativePath = inputDir.relativize(filePath)
        val parent = relativePath.parent ?: Paths.get(".")
        val (baseName, extension) = splitExt(relativePath.fileName)
        val outputBase = outputDir.resolve(parent)
        Files.createDirectories(outputBase)
        val reportPath = outputBase.resolve("$baseName-testability.json")
        val dataInputLines = mutableListOf<String>()
        val emit: (String) -> Unit = { dataInputLines.add(it) }
        val rawJvmClass = Files.newInputStream(filePath).use { inputStream ->
            val dataInput = DataInputStream(inputStream)
            val debugDataInput = DebugDataInput(dataInput, emit)
            RawJvmClass.fromDataInput(debugDataInput)
        }
        val jvmClass = JvmClass.fromRawJvmClass(rawJvmClass, events)
        val testabiltyReport = TestabilityReport.fromJvmClass(filePath,jvmClass,isOnBlackList, isOnWhiteList)
        val json = JsonMappers.pretty.writeValueAsString(testabiltyReport)
        Files.writeString(reportPath, json, StandardOpenOption.CREATE)
    }

    fun loadRegexPredicate(path:Path): (String)->Boolean{
        val patterns = Files.readAllLines(path).map { it.trim() }.filter { it.isNotEmpty() }
        return AcceptList.fromPatterns(patterns)
    }

    class ParseVisitor(private val inputDir: Path,
                       private val isOnBlackList:(String)->Boolean,
                       private val isOnWhiteList:(String)->Boolean,
                       private val events: Events) : FileVisitor<Path> {
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
            parseFile(inputDir, file, isOnBlackList, isOnWhiteList, events)
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
