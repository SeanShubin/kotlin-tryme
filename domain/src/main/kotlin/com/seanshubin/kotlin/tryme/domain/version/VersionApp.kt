package com.seanshubin.kotlin.tryme.domain.version

object VersionApp {
    data class Versions(val prod: String, val staging: String)

    @JvmStatic
    fun main(args: Array<String>) {
        var versions = Versions("1.1.0", "1.0.0-5")
        println(versions)
        (1 until 5).forEach {
            versions = updateVersions(versions)
            println(versions)
        }
    }

    val prodPattern = """(\d+)\.(\d+)\.(\d+)"""
    val prodRegex = prodPattern.toRegex()
    val stagingPattern = """$prodPattern-(\d+)"""
    val stagingRegex = stagingPattern.toRegex()

    private fun toStagingParts(s: String): List<Int> =
        toIntParts(stagingRegex, s)

    private fun toProdParts(s: String): List<Int> =
        toIntParts(prodRegex, s)

    private fun toIntParts(r:Regex, s: String): List<Int> {
        val result = r.matchEntire(s) ?: throw RuntimeException("'$s' does not match '$r'")
        return result.groupValues.drop(1).map { it.toInt() }
    }

    private fun updateVersions(versions: Versions): Versions {
        val prodParts = toProdParts(versions.prod)
        val stagingParts = toStagingParts(versions.staging)
        val stagingPrefix = stagingParts.take(3)
        if(stagingPrefix == prodParts){
            val oldStagingSuffix = stagingParts[3]
            val newStagingSuffix = oldStagingSuffix + 1
            val stagingVersion = stagingPrefix.joinToString(".") + "-" + newStagingSuffix.toString()
            val prodVersion = prodParts.joinToString(".")
            return Versions(prodVersion, stagingVersion)
        } else {
            val newStagingSuffix = 0
            val stagingVersion = prodParts.joinToString(".") + "-" + newStagingSuffix.toString()
            val prodVersion = prodParts.joinToString(".")
            return Versions(prodVersion, stagingVersion)
        }
    }
}
