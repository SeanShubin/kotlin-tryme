package com.seanshubin.kotlin.tryme.domain.pathtree

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import java.nio.file.Paths

object PathTreeApp {
  @JvmStatic
  fun main(args: Array<String>) {
    val filesContract: FilesContract = FilesDelegate
    val basePath = Paths.get("domain/src/main")
    val pathTree = PathTree.create(filesContract, basePath, depth = 0)
    val paths = pathTree.paths()
    val directories = pathTree.directoryPaths()
    val files = pathTree.filePaths()
    pathTree.forEach {
      println("  ".repeat(it.depth) + it.name)
    }
  }
}
