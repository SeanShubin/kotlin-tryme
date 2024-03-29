package com.seanshubin.kotlin.tryme.domain.downloader

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import java.net.URI
import java.nio.file.Path

class Downloader(
    private val baseUri:URI,
    private val downloadDir:Path,
    private val http: HttpContract,
    private val linkParser: LinkParser,
    private val uriPolicies:List<UriPolicy>,
    private val persistence: Persistence,
    private val files:FilesContract,
    private val headers:List<Pair<String, String>>
) {
    private var state = DownloadState.empty

    fun downloadSite() {
        downloadSite(baseUri)
    }

    private fun downloadSite(uri:URI){
        val result = http.getString(uri, headers)
        if (result.isNotOk()) return
        val links = linkParser.parseLinks(uri, result.text).filter{link ->
            link.host == baseUri.host
        }
        val linksByPolicyNames = links.map { link ->
            val policyNames = uriPolicies.filter { policy ->
                policy.accept(link)
            }.map { it.name }
            Pair(policyNames, link)
        }.groupBy { it.first }.map { (policyNames, entry) ->
            Pair(policyNames, entry.map{it.second})
        }.toMap()
        linksByPolicyNames.forEach(::recordLinkStatus)
        val linksToFollow = linksByPolicyNames[listOf("follow")] ?: emptyList()
        linksToFollow.forEach(::followLinkIfNotAlreadyFollowed)
        val linksToDownload = linksByPolicyNames[listOf("download")] ?: emptyList()
        linksToDownload.forEach(::downloadLinkIfNotAlreadyDownloaded)
    }

    private fun recordLinkStatus(entry: Map.Entry<List<String>, List<URI>>) {
        val names = entry.key
        val links = entry.value
        val setName = if(names.isEmpty()) {
            "uncategorized"
        } else {
            names.joinToString("-")
        }
        val linkNames = links.map{it.toString()}
        persistence.addToSet(setName, linkNames)
    }

    private fun followLinkIfNotAlreadyFollowed(link: URI) {
        if(!state.alreadyFollowed.contains(link)){
            state = state.addToAlreadyFollowed(link)
            downloadSite(link)
        }
    }

    private fun downloadLinkIfNotAlreadyDownloaded(link: URI) {
        val path = linkToPath(link)
        if(files.exists(path)) {
            println("already exists: $link -> $path")
        } else {
            println("downloading: $link -> $path")
            FilesUtil.ensureParentExists(files, path)
            http.download(link, headers, path)
        }
    }

    private fun linkToPath(link:URI):Path {
        val path = downloadDir.resolve(link.path.substring(1))
        return path
    }
}
