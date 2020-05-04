package com.seanshubin.kotlin.tryme.domain.tree

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import java.nio.file.Path
import kotlin.streams.toList

object PathTreeFactory {
  fun create(files: FilesContract, path: Path): Tree<Path> {
    val children = if (files.isDirectory(path)) {
      files.list(path).map {
        create(files, it)
      }.toList()
    } else {
      emptyList()
    }
    return Tree(path, children)
  }
}
