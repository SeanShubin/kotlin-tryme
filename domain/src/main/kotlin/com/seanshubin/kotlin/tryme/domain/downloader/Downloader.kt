package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI
import java.nio.file.Path

class Downloader(
    private val baseUri:URI,
    private val downloadDir:Path,
    private val http: HttpContract,
    private val linkParser: LinkParser,
    private val uriPolicies: List<UriPolicy>,
    private val persistence: Persistence
) {
    private var state = DownloadState.empty
    private val uriPolicyMap = uriPolicies.associateBy { it.name }

    fun downloadSite() {
        downloadSite(baseUri)
    }

    private fun downloadSite(uri:URI){
        val result = http.getString(uri)
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
}
