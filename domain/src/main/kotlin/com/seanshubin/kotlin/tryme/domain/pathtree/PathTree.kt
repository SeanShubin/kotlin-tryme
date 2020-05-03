package com.seanshubin.kotlin.tryme.domain.pathtree

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import java.nio.file.Path
import kotlin.streams.toList

interface PathTree {
  val name: String
  val path: Path
  val depth: Int
  fun paths(): List<Path>
  fun directoryPaths(): List<Path>
  fun filePaths(): List<Path>
  fun forEach(f: (PathTree) -> Unit)

  companion object {
    fun create(files: FilesContract, basePath: Path, depth: Int): PathTree {
      val name = basePath.fileName.toString()
      return if (files.isDirectory(basePath)) {
        val list = files.list(basePath)
        val children = list.map {
          create(files, it, depth + 1)
        }.toList()
        PathTreeDirectory(name, basePath, depth, children)
      } else {
        PathTreeFile(name, basePath, depth)
      }
    }
  }
}
