package com.seanshubin.kotlin.tryme.domain.downloader

import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import java.net.URI
import java.net.URISyntaxException
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
        val linkParser = LinkParserHtml(::uriSyntaxException)
        val shouldFollow = PatternMatcher("follow", listOf(""".*\.html$"""))
        val shouldDownload = PatternMatcher("download", emptyList())
        val uriPolicies = listOf(shouldFollow, shouldDownload)
        val downloader = Downloader(
            site,
            downloadDir,
            httpCached,
            linkParser,
            uriPolicies,
            persistence)
        downloader.downloadSite()
    }

    fun uriSyntaxException(uriString:String, ex:URISyntaxException){
        println("URISyntaxException: $uriString" )
    }
}
