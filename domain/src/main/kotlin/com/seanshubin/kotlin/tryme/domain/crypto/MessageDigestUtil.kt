package com.seanshubin.kotlin.tryme.domain.crypto

import java.security.Security

object MessageDigestUtil {
  fun listMessageDigestAlgorithms(): List<String> =
      Security.getProviders().flatMap { provider ->
        provider.services.flatMap { service ->
          val algorithm = service.algorithm
          val type = service.type
          if (type == "MessageDigest") {
            listOf(algorithm)
          } else {
            emptyList()
          }
        }
      }
}