package com.seanshubin.kotlin.tryme.domain.pathtree

import java.nio.file.Path
import java.nio.file.Paths

data class PathTreeDirectory(override val name: String,
                             override val path: Path,
                             override val depth: Int,
                             val children: List<PathTree>) : PathTree {
  override fun paths(): List<Path> {
    val thisPath = Paths.get(name)
    val childPaths = children.flatMap { it.paths() }.map { thisPath.resolve(it) }
    return thisPath + childPaths
  }

  override fun directoryPaths(): List<Path> {
    val thisPath = Paths.get(name)
    val childPaths = children.flatMap { it.directoryPaths() }.map { thisPath.resolve(it) }
    return thisPath + childPaths
  }

  override fun filePaths(): List<Path> {
    val thisPath = Paths.get(name)
    val childPaths = children.flatMap { it.filePaths() }.map { thisPath.resolve(it) }
    return childPaths
  }

  override fun forEach(f: (PathTree) -> Unit) {
    f(this)
    children.forEach { it.forEach(f) }
  }
}
