package com.seanshubin.kotlin.tryme.domain.downloader

import com.seanshubin.kotlin.tryme.domain.config.JsonFileConfiguration
import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import java.net.URI
import java.net.URISyntaxException
import java.net.http.HttpClient
import java.nio.file.Paths
import java.time.Clock

object DownloaderSampleApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val configurationPathName = args.getOrNull(0) ?: "downloader-config.json"
        val configurationPath = Paths.get(configurationPathName)
        val files = FilesDelegate
        val configuration = JsonFileConfiguration(files, configurationPath)
        val site = URI(configuration.stringAt("site to scan", listOf("site")).load())
        val cacheDir = Paths.get(configuration.stringAt("generated/cache", listOf("cacheDir")).load())
        val reportDir = Paths.get(configuration.stringAt("generated/report", listOf("reportDir")).load())
        val downloadDir =
            Paths.get(configuration.stringAt("generated/download", listOf("downloadDir")).load())
        val httpClient = HttpClient.newHttpClient()
        val httpContract = HttpDelegate(httpClient)
        val clock = Clock.systemUTC()
        val httpCached = HttpCached(httpContract, clock, cacheDir, files)
        val persistence = PersistenceToFile(reportDir, files)
        val linkParser = LinkParserHtml(::uriSyntaxException)
        val followPatterns = configuration.stringListAt(listOf(""".*"""), listOf("patterns", "follow")).load()
        val downloadPatterns = configuration.stringListAt(emptyList<String>(), listOf("patterns", "download")).load()
        val uriPoliciesSpecification: Map<String, List<String>> = mapOf(
            "follow" to followPatterns,
            "download" to downloadPatterns
        )
        val downloader = Downloader(
            site,
            downloadDir,
            httpCached,
            linkParser,
            uriPoliciesSpecification,
            persistence,
            files
        )
        downloader.downloadSite()
    }

    fun uriSyntaxException(uriString: String, ex: URISyntaxException) {
        println("URISyntaxException: $uriString")
    }
}
