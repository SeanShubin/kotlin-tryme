package com.seanshubin.kotlin.tryme.domain.crypto

import com.seanshubin.kotlin.tryme.domain.bytearray.ByteArrayUtil.toHexString
import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import com.seanshubin.kotlin.tryme.domain.crypto.MessageDigestUtil.listMessageDigestAlgorithms
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest

object ashFilesApp {
  // 5bae608dbfeba57e74aa01a18e614900
  /*

  MessageDigest.SHA-256 ImplementedIn
  MessageDigest\.([A-Z0-9\-])(?: ImplementedIn)?

   */
  val messageDigestRegex = Regex("""MessageDigest\.([A-Z0-9\-/]+)(?: ImplementedIn)?""")

  @JvmStatic
  fun main(args: Array<String>) {
    val files: FilesContract = FilesDelegate
    val path: Path = Paths.get(args[0])
    listMessageDigestAlgorithms().forEach {
      val messageDigest = MessageDigest.getInstance(it)
      files.newInputStream(path).use { inputStream ->
        val block = ByteArray(4096)
        var byteCount = inputStream.read(block)
        while (byteCount != -1) {
          messageDigest.update(block, 0, byteCount)
          byteCount = inputStream.read(block)
        }
        val hex = messageDigest.digest().toHexString()
        println("$it $hex")
      }

    }

  }


  fun parseMessageDigestAlgorithmFromSecurityProviderPropertyName(securityProviderPropertyName: String): String {
    val matchResult = messageDigestRegex.matchEntire(securityProviderPropertyName)
    if (matchResult == null) {
      throw RuntimeException("value '$securityProviderPropertyName' did not match regex '$messageDigestRegex'")
    } else {
      return matchResult.groupValues[1]
    }
  }
}
