package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI
import java.nio.file.Path

class Downloader(
    private val baseUri:URI,
    private val downloadDir:Path,
    private val http: HttpContract,
    private val linkParser: LinkParser,
    private val shouldFollow: (URI) -> Boolean,
    private val shouldDownload: (URI) -> Boolean,
    private val persistence: Persistence
) {
    private var state = DownloadState.empty

    fun downloadSite() {
        downloadSite(baseUri)
    }

    private fun downloadSite(uri:URI){
        val result = http.getString(uri)
        if (result.isNotOk()) return
        val links = linkParser.parseLinks(uri, result.text).filter{link ->
            link.host == baseUri.host
        }
        val linksToFollow = mutableListOf<URI>()
        val linksToDownload = mutableListOf<URI>()
        val linksMatchingBoth = mutableListOf<URI>()
        val linksMatchingNeither = mutableListOf<URI>()
        links.forEach { link ->
            if (shouldFollow(link)) {
                if (shouldDownload(link)) {
                    linksMatchingBoth.add(link)
                } else {
                    linksToFollow.add(link)
                }
            } else {
                if (shouldDownload(link)) {
                    linksToDownload.add(link)
                } else {
                    linksMatchingNeither.add(link)
                }
            }
        }
        linksMatchingNeither.forEach(::processLinkMatchingNeither)
        linksMatchingBoth.forEach(::processLinkMatchingBoth)
        linksToFollow.forEach(::processLinkToFollow)
        linksToDownload.forEach(::processLinkToDownload)
    }

    private fun processLinkMatchingBoth(link: URI) {
        persistence.addToSet("links-matching-both", link.toString())
    }

    private fun processLinkMatchingNeither(link: URI) {
        persistence.addToSet("links-matching-neither", link.toString())
    }

    private fun processLinkToFollow(link: URI) {
        if(!state.alreadyFollowed.contains(link)){
            persistence.addToSet("links-matching-follow", link.toString())
            state = state.addToAlreadyFollowed(link)
            downloadSite(link)
        }
    }

    private fun processLinkToDownload(link: URI) {
        persistence.addToSet("links-matching-download", link.toString())
    }
}