package com.seanshubin.kotlin.tryme.domain.unicode.hiragana

enum class HiraganaType {
    UNUSED{
        override fun create(char: Char): Hiragana? {
            val codePoint = char.toInt()
            val name = Character.getName(codePoint)
            return if(name == null) HiraganaUnused(char)
            else null
        }
    },
    SMALL_LETTER{
        override fun create(char: Char): Hiragana? {
            val codePoint = char.toInt()
            val name = Character.getName(codePoint) ?: return null
            val match = hiraganaSmallLetterRegex.matchEntire(name) ?: return null
            val romaji = match.groupValues[1]
            return HiraganaLetterSmall(char, romaji, name)
        }
    },
    LETTER{
        override fun create(char: Char): Hiragana? {
            val codePoint = char.toInt()
            val name = Character.getName(codePoint) ?: return null
            val match = hiraganaLetterRegex.matchEntire(name) ?: return null
            val romaji = match.groupValues[1]
            return HiraganaLetter(char, romaji, name)
        }
    },
    OTHER{
        override fun create(char: Char): Hiragana? {
            val codePoint = char.toInt()
            val name = Character.getName(codePoint) ?: return null
            return HiraganaOther(char, name)
        }
    };
    abstract fun create(char:Char): Hiragana?
    companion object {
        private val hiraganaLetterRegex = Regex("""HIRAGANA LETTER (\w+)""")
        private val hiraganaSmallLetterRegex = Regex("""HIRAGANA LETTER SMALL (\w+)""")
    }
}