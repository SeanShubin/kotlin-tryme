package com.seanshubin.kotlin.tryme.domain.filefinder

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import java.nio.file.FileVisitResult
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

class CollectRelevantFileVisitor(
    private val isRelevant: (Path) -> Boolean,
    private val relevantFileFound: (Path) -> Unit = {}
) : DoNothingFileVisitor() {
    private val mutableFilesFoundList = mutableListOf<Path>()
    val filesFoundList: List<Path> = mutableFilesFoundList

    override fun visitFile(file: Path, attrs: BasicFileAttributes?): FileVisitResult {
        if (isRelevant(file)) {
            mutableFilesFoundList.add(file)
            relevantFileFound(file)
        }
        return FileVisitResult.CONTINUE
    }

    fun findRelevantFiles(files: FilesContract, baseDir: Path): List<Path> {
        val visitor = CollectRelevantFileVisitor(isRelevant, relevantFileFound)
        files.walkFileTree(baseDir, visitor)
        val filesFound = visitor.filesFoundList
        return filesFound
    }
}
