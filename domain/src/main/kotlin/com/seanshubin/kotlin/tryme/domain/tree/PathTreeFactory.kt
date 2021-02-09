package com.seanshubin.kotlin.tryme.domain.tree

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import java.nio.file.Path
import kotlin.streams.toList

object PathTreeFactory {
  fun create(files: FilesContract, path: Path, accept: (Path) -> Boolean): Tree<Path> {
    val children = if (files.isDirectory(path)) {
      files.list(path).toList().flatMap {
        if (accept(it)) {
          listOf(create(files, it, accept))
        } else {
          emptyList()
        }
      }
    } else {
      emptyList()
    }
    return Tree(path, children)
  }
}
