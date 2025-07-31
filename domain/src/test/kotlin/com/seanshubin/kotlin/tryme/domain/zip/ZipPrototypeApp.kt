package com.seanshubin.kotlin.tryme.domain.zip

import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipEntry

object ZipPrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
//        val zipFile = Paths.get("data", "data.zip")
//        val zipFile = Paths.get("data", "java.base.zip")
        val zipFile = Paths.get("data", "java.base.jmod")
        Files.newInputStream(zipFile).use { inputStream ->
            val name = zipFile.toString()
            val isZip = { name: String -> name.endsWith(".zip") }
            val accept = { path:List<String>, zipEntry:ZipEntry ->
                true
            }
            val iterator = ZipContentsIterator(inputStream, name, isZip, accept)
            iterator.forEach { cursor ->
                val (path, zipEntry, bytes) = cursor
                val pathString = path.joinToString("/")
                println("$pathString $zipEntry ${zipEntry.method} ${bytes.size}")
            }
        }
    }
}