package com.seanshubin.kotlin.tryme.domain.filefinder

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import java.nio.file.FileVisitor
import java.nio.file.Path
import java.nio.file.Paths

object FileFinderPrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val baseDir = Paths.get(".")
        val includeRegexPatterns = listOf(""".*\.kt$""")
        val excludeRegexPatterns = listOf(
            """.*/src/test/kotlin/.*""",
            """\./\.idea/.*"""
        )
        val files:FilesContract = FilesDelegate
        val accept:(Path)->Boolean = RegexPathFilter(includeRegexPatterns, excludeRegexPatterns)
        val visitor = CollectRelevantFileVisitor(accept)
        files.walkFileTree(baseDir, visitor)
        val fileList = visitor.filesFoundList
        fileList.forEach(::println)
    }
}
