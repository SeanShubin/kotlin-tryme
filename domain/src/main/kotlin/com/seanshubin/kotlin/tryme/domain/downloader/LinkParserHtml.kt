package com.seanshubin.kotlin.tryme.domain.downloader

import java.net.URI

class LinkParserHtml:LinkParser {
    override fun parseLinks(source:URI, text: String): List<URI> {
        return linkRegex.findAll(text).map { matchResult ->
            URI(matchResult.groupValues[1])
        }.map { link ->
            toAbsoluteLink(source, link)
        }.toList()
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
