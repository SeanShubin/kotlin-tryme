package com.seanshubin.kotlin.tryme.domain.downloader

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.json.JsonMappers
import java.net.URI
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Clock

class HttpCached(
    private val httpContract: HttpContract,
    private val clock: Clock,
    private val cacheBaseDir: Path,
    private val files: FilesContract
) : HttpContract {
    private val cacheTableOfContentsPath =cacheBaseDir.resolve("table-of-contents.json")
    override fun getString(uri: URI): HttpStringResult {
        val cacheTableOfContents = loadCacheTableOfContents()
        val cachedResult = loadFromCache(uri, cacheTableOfContents)
        val result = if (cachedResult == null) {
            val nonCachedResult = httpContract.getString(uri)
            storeToCache(uri, nonCachedResult, cacheTableOfContents)
            nonCachedResult
        } else {
            cachedResult
        }
        return result
    }

    override fun download(uri: URI, path: Path) {
        httpContract.download(uri, path)
    }

    private fun loadFromCache(uri: URI, cacheTableOfContents: CacheTableOfContents): HttpStringResult? {
        val cacheEntry = cacheTableOfContents.entries[uri]
        return if (cacheEntry == null) {
            null
        } else {
            val path = Paths.get(cacheEntry.relativePath)
            val text = files.readString(path)
            val statusCode = cacheEntry.statusCode
            HttpStringResult(statusCode, text)
        }
    }

    private fun storeToCache(uri: URI, result: HttpStringResult, cacheTableOfContents: CacheTableOfContents) {
        if(uri.path=="") return
        val whenCached = clock.instant()
        val statusCode = result.statusCode
        val relativePath = relativePathFor(uri)
        val entry = CacheEntry(uri, whenCached, statusCode, relativePath.toString())
        val newTableOfContents = cacheTableOfContents.addEntry(entry)
        FilesUtil.ensureParentExists(files, relativePath)
        storeCacheTableOfContents(newTableOfContents)
        files.writeString(relativePath, result.text)
    }

    private fun loadCacheTableOfContents(): CacheTableOfContents {
        initializeTableOfContentsFileIfNecessary(cacheTableOfContentsPath)
        val text = files.readString(cacheTableOfContentsPath)
        val tableOfContents = JsonMappers.parser.readValue<CacheTableOfContents>(text)
        return tableOfContents
    }
    private fun storeCacheTableOfContents(cacheTableOfContents: CacheTableOfContents) {
        initializeTableOfContentsFileIfNecessary(cacheTableOfContentsPath)
        val text = JsonMappers.pretty.writeValueAsString(cacheTableOfContents)
        files.writeString(cacheTableOfContentsPath, text)
    }

    private fun initializeTableOfContentsFileIfNecessary(path:Path){
        if(files.exists(path)) return
        FilesUtil.ensureParentExists(files, path)
        val text = JsonMappers.pretty.writeValueAsString(CacheTableOfContents.empty)
        files.writeString(path, text)
    }

    fun relativePathFor(uri:URI):Path {
        val result = if(uri.path.startsWith("/")){
            cacheBaseDir.resolve(uri.host).resolve(uri.path.substring(1))
        } else {
            throw UnsupportedOperationException("not implemented")
        }
        return result
    }
}