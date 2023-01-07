package com.seanshubin.kotlin.tryme.domain.downloader

import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import java.net.URI
import java.net.http.HttpClient
import java.nio.file.Paths
import java.time.Clock

object DownloaderSampleApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val site = URI("https://reactjs.org")
        val cacheDir = Paths.get("generated", "http-sample", "cache")
        val reportDir = Paths.get("generated", "http-sample", "report")
        val downloadDir = Paths.get("generated", "http-sample", "download")

        val httpClient = HttpClient.newHttpClient()
        val httpContract = HttpDelegate(httpClient)
        val clock = Clock.systemUTC()
        val files = FilesDelegate
        val httpCached = HttpCached(httpContract, clock, cacheDir, files)
        val persistence = PersistenceToFile(reportDir, files)
        val linkParser = LinkParserHtml()
        val followPatterns = listOf<String>(""".*\.html$""")
        val downloadPatterns = listOf<String>()
        val shouldFollow = PatternMatcher(followPatterns)
        val shouldDownload = PatternMatcher(downloadPatterns)
        val downloader = Downloader(
            site,
            downloadDir,
            httpCached,
            linkParser,
            shouldFollow,
            shouldDownload,
            persistence)
        downloader.downloadSite()
    }
}