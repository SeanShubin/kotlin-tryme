package com.seanshubin.kotlin.tryme.domain.json.editor

import com.seanshubin.kotlin.tryme.domain.contract.FilesContractUnsupportedOperation
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.attribute.FileAttribute

class FilesFake:FilesContractUnsupportedOperation {
    val existingDirectories = mutableSetOf<Path>()
    val fileContents = mutableMapOf<Path, String>()
    override fun createDirectories(dir: Path, vararg attrs: FileAttribute<*>): Path {
        var currentDir:Path? = dir
        while(currentDir != null){
            existingDirectories.add(dir)
            currentDir = currentDir.parent
        }
        return dir
    }

    override fun writeString(path: Path, csq: CharSequence, vararg options: OpenOption): Path {
        fileContents[path] = csq.toString()
        return path
    }

    override fun readString(path: Path): String {
        return fileContents.getValue(path)
    }
}
