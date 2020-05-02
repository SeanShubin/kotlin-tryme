package com.seanshubin.kotlin.tryme.domain.pathutil

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

fun Path.listFilesRecursive(files: FilesContract): List<Path> {
  val visitor = object : FileVisitor<Path> {
    val collected = mutableListOf<Path>()
    override fun postVisitDirectory(dir: Path?, exc: IOException?): FileVisitResult =
        FileVisitResult.CONTINUE

    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult =
        FileVisitResult.CONTINUE

    override fun visitFileFailed(file: Path?, exc: IOException?): FileVisitResult =
        FileVisitResult.CONTINUE

    override fun preVisitDirectory(dir: Path?, attrs: BasicFileAttributes?): FileVisitResult =
        FileVisitResult.CONTINUE
  }
  files.walkFileTree(this, visitor)
  return visitor.collected
}
