package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI

data class DownloadState(val alreadyFollowed: List<URI>) {
    fun addToAlreadyFollowed(link: URI): DownloadState =
        copy(alreadyFollowed = alreadyFollowed + link)

    companion object {
        val empty = DownloadState(alreadyFollowed = emptyList())
    }
}
