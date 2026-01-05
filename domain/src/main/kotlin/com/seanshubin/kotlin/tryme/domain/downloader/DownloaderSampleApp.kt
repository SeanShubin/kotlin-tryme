package com.seanshubin.kotlin.tryme.domain.downloader

import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import com.seanshubin.kotlin.tryme.domain.dynamic.FixedPathJsonFileKeyValueStore
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
        val configuration = FixedPathJsonFileKeyValueStore(files, configurationPath)
        val site = URI(configuration.loadOrDefault(listOf("site"), "site to scan") as String)
        val cacheDir = Paths.get(configuration.loadOrDefault(listOf("cacheDir"), "generated/cache") as String)
        val reportDir = Paths.get(configuration.loadOrDefault(listOf("reportDir"), "generated/report") as String)
        val username = configuration.loadOrDefault(listOf("username"), "username") as String
        val password = configuration.loadOrDefault(listOf("password"), "password") as String
        val downloadDir =
            Paths.get(configuration.loadOrDefault(listOf("downloadDir"), "generated/download") as String)
        val httpClient = HttpClient.newHttpClient()
        val httpContract = HttpDelegate(httpClient)
        val clock = Clock.systemUTC()
        val httpCached = HttpCached(httpContract, clock, cacheDir, files)
        val persistence = PersistenceToFile(reportDir, files)
        val linkParser = LinkParserHtml(::uriSyntaxException, ::uriPathIsNull)
        val followPatternsInclude = configuration.loadOrDefault(listOf("patterns", "follow", "include"), listOf(""".*""")) as List<String>
        val followPatternsExclude = configuration.loadOrDefault(listOf("patterns", "follow", "exclude"), emptyList<String>()) as List<String>
        val downloadPatternsInclude = configuration.loadOrDefault(listOf("patterns", "download", "include"), emptyList<String>()) as List<String>
        val downloadPatternsExclude = configuration.loadOrDefault(listOf("patterns", "download", "exclude"), emptyList<String>()) as List<String>
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
