package com.seanshubin.kotlin.tryme.domain.downloader

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import java.nio.file.Path

object FilesUtil {
    fun ensureParentExists(files: FilesContract, path: Path){
        val parent = path.parent ?: return
        files.createDirectories(parent)
    }
}
