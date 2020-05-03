package com.seanshubin.kotlin.tryme.domain.pathtree

import java.nio.file.Path
import java.nio.file.Paths

data class PathTreeFile(override val name: String,
                        override val path: Path,
                        override val depth: Int) : PathTree {
  override fun paths(): List<Path> {
    return listOf(Paths.get(name))
  }

  override fun directoryPaths(): List<Path> {
    return emptyList()
  }

  override fun filePaths(): List<Path> {
    return listOf(Paths.get(name))
  }

  override fun forEach(f: (PathTree) -> Unit) {
    f(this)
  }
}
