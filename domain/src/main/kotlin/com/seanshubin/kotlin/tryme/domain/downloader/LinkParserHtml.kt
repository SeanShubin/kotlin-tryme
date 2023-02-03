package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI
import java.net.URISyntaxException

class LinkParserHtml(
    private val uriSyntaxException:(String, URISyntaxException)->Unit,
    private val uriPathIsNull:(URI)->Unit
):LinkParser {
    override fun parseLinks(source:URI, text: String): List<URI> {
        return linkRegex.findAll(text).mapNotNull { matchResult ->
            val uriString = dropFragment(matchResult.groupValues[1])
            try {
                val uri = URI(uriString)
                if(uri.path == null){
                    uriPathIsNull(uri)
                    null
                } else {
                    uri
                }
            } catch(ex: URISyntaxException){
                uriSyntaxException(uriString, ex)
                null
            }
        }.map { link ->
            toAbsoluteLink(source, link).normalize()
        }.toList()
    }

    private fun dropFragment(link:String):String {
        val fragmentIndex = link.indexOf('#')
        return if(fragmentIndex == -1){
            link
        } else {
            link.substring(0, fragmentIndex)
        }
    }

    private fun toAbsoluteLink(source:URI, link:URI):URI{
        return if(link.host == null){
            if(link.path.startsWith("/")){
                URI(
                    source.scheme,
                    source.userInfo,
                    source.host,
                    source.port,
                    link.path,
                    link.query,
                    link.fragment
                )
            } else {
                val newPath = discardLastSlashAndAfter(source.path) + "/" + link.path
                URI(
                    source.scheme,
                    source.userInfo,
                    source.host,
                    source.port,
                    newPath,
                    link.query,
                    link.fragment
                )
            }
        } else {
            link
        }
    }

    private fun discardLastSlashAndAfter(s:String):String {
        val index = s.lastIndexOf('/')
        if(index == -1) return s
        return s.substring(0, index)
    }

    companion object {
        val linkRegex = Regex("""<a .*\bhref="([^"]+)"[^>]*""")
    }
}
