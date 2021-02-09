package com.seanshubin.kotlin.tryme.domain.tree

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import java.nio.file.Path
import java.nio.file.Paths

object PathTreeApp {
  @JvmStatic
  fun main(args: Array<String>) {
    val filesContract: FilesContract = FilesDelegate
    val basePath = Paths.get("sample-data/tree-sample")
    val pathTree = PathTreeFactory.create(filesContract, basePath) { _ -> true }
    val coordinateString: (Path, List<Path>, List<Int>) -> String = { value, ancestors, indices ->
      "$value, ancestors = $ancestors, indices = $indices"
      "  ".repeat(indices.size) + value.fileName.toString()
    }
    pathTree.mapWithCoordinates(coordinateString).forEach { println(it.value) }
  }
}
