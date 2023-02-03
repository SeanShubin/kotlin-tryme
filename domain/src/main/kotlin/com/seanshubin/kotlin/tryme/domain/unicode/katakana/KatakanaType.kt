package com.seanshubin.kotlin.tryme.domain.unicode.katakana

import com.seanshubin.kotlin.tryme.domain.unicode.katakana.*

enum class KatakanaType {
    UNUSED{
        override fun create(char: Char): Katakana? {
            val codePoint = char.toInt()
            val name = Character.getName(codePoint)
            return if(name == null) KatakanaUnused(char, codePoint)
            else null
        }
    },
    SMALL_LETTER{
        override fun create(char: Char): Katakana? {
            val codePoint = char.toInt()
            val name = Character.getName(codePoint) ?: return null
            val match = katakanaSmallLetterRegex.matchEntire(name) ?: return null
            val romaji = match.groupValues[1]
            return KatakanaLetterSmall(char, codePoint, name, romaji)
        }
    },
    LETTER{
        override fun create(char: Char): Katakana? {
            val codePoint = char.toInt()
            val name = Character.getName(codePoint) ?: return null
            val match = katakanaLetterRegex.matchEntire(name) ?: return null
            val romaji = match.groupValues[1]
            return KatakanaLetter(char, codePoint, name, romaji)
        }
    },
    OTHER{
        override fun create(char: Char): Katakana? {
            val codePoint = char.toInt()
            val name = Character.getName(codePoint) ?: return null
            return KatakanaOther(char, codePoint, name)
        }
    };
    abstract fun create(char:Char): Katakana?
    companion object {
        private val katakanaLetterRegex = Regex("""KATAKANA LETTER (\w+)""")
        private val katakanaSmallLetterRegex = Regex("""KATAKANA LETTER SMALL (\w+)""")
    }
}