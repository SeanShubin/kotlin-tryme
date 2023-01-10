package com.seanshubin.kotlin.tryme.domain.downloader

import com.seanshubin.kotlin.tryme.domain.config.JsonFileConfiguration
import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import java.net.URI
import java.net.URISyntaxException
import java.net.http.HttpClient
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.time.Clock
import java.util.*

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
        val username = configuration.stringAt("username", listOf("username")).load()
        val password = configuration.stringAt("password", listOf("password")).load()
        val downloadDir =
            Paths.get(configuration.stringAt("generated/download", listOf("downloadDir")).load())
        val httpClient = HttpClient.newHttpClient()
        val httpContract = HttpDelegate(httpClient)
        val clock = Clock.systemUTC()
        val httpCached = HttpCached(httpContract, clock, cacheDir, files)
        val persistence = PersistenceToFile(reportDir, files)
        val linkParser = LinkParserHtml(::uriSyntaxException, ::uriPathIsNull)
        val followPatternsInclude = configuration.stringListAt(listOf(""".*"""), listOf("patterns", "follow", "include")).load()
        val followPatternsExclude = configuration.stringListAt(emptyList<String>(), listOf("patterns", "follow", "exclude")).load()
        val downloadPatternsInclude = configuration.stringListAt(emptyList<String>(), listOf("patterns", "download", "include")).load()
        val downloadPatternsExclude = configuration.stringListAt(emptyList<String>(), listOf("patterns", "download", "exclude")).load()
        val uriPolicies = listOf(
            PatternMatcher("follow", followPatternsInclude, followPatternsExclude),
            PatternMatcher("download", downloadPatternsInclude, downloadPatternsExclude)
        )
        val encoder: Base64.Encoder = Base64.getEncoder()
        val basic = "$username:$password"
        val charset = StandardCharsets.UTF_8
        val encodedBasic = String(encoder.encode(basic.toByteArray(charset)), charset)
        val authHeader = "Basic $encodedBasic"
        val headers = listOf("authorization" to authHeader)
        val downloader = Downloader(
            site,
            downloadDir,
            httpCached,
            linkParser,
            uriPolicies,
            persistence,
            files,
            headers
        )
        downloader.downloadSite()
    }

    fun uriSyntaxException(uriString: String, ex: URISyntaxException) {
        println("URISyntaxException: $uriString")
    }

    fun uriPathIsNull(uri: URI) {
        println("uri path is null: $uri")
    }
}
