package com.seanshubin.kotlin.tryme.domain.diskusagekotlin

import com.seanshubin.kotlin.tryme.domain.diskusagekotlin.Entity.Companion.sizeDescending
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object EntryPoint {
    @JvmStatic
    fun main(args: Array<String>) {
        val directoryName = args[0]
        val directory= Paths.get(directoryName)
        scanDirectoryRecursively(directory)
    }

    fun scanDirectoryRecursively(directory: Path){
        val entities = scanAllEntities(directory).sortedWith(sizeDescending)
        val lines = entities.map(::entityToLine)
        lines.forEach(::println)
    }

    fun scanAllEntities(path:Path):List<Entity>{
        return if(Files.isRegularFile(path)){
            scanRegularFile(path)
        } else if(Files.isDirectory(path)){
            scanDirectory(path)
        } else {
            throw RuntimeException("Don't know what to do with $path")
        }
    }

    fun scanRegularFile(file:Path):List<Entity>{
        val sizeOfContents = Files.size(file)
        val fileCount =1
        val dirCount = 0
        val entity = Entity(sizeOfContents, fileCount, dirCount, file)
        return listOf(entity)
    }

    fun scanDirectory(dir:Path):List<Entity>{

        val remainder =  Files.list(dir).toList().flatMap { path ->
            scanAllEntities(path)
        }.toList()
        val sizeOfContents = remainder.map { it.sizeOfContents }.sum()
        val fileCount = remainder.count { Files.isRegularFile(it.path) }
        val dirCount = remainder.count { Files.isDirectory(it.path)}
        val thisDir = Entity(sizeOfContents, fileCount, dirCount, dir)
        return listOf(thisDir) + remainder
    }

    fun entityToLine(entity:Entity):String {
        return "bytes: ${entity.sizeOfContents} files: ${entity.fileCount} dirs: ${entity.dirCount} ${entity.path}"
    }
}