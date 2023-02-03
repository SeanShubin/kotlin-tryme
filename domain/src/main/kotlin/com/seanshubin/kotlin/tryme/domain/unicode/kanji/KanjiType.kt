package com.seanshubin.kotlin.tryme.domain.unicode.kanji

enum class KanjiType {
    UNUSED {
        override fun create(char: Char): Kanji? {
            val codePoint = char.toInt()
            val name = Character.getName(codePoint)
            return if (name == null) KanjiUnused(char, codePoint)
            else null
        }
    },
    LETTER {
        override fun create(char: Char): Kanji? {
            val codePoint = char.toInt()
            val name = Character.getName(codePoint) ?: return null
            val match = kanjiLetterRegex.matchEntire(name) ?: return null
            val id = match.groupValues[1]
            return KanjiLetter(char, codePoint, name, id)
        }
    },
    OTHER {
        override fun create(char: Char): Kanji? {
            val codePoint = char.toInt()
            val name = Character.getName(codePoint) ?: return null
            return KanjiOther(char, codePoint, name)
        }
    };

    abstract fun create(char: Char): Kanji?

    companion object {
        private val kanjiLetterRegex = Regex("""CJK UNIFIED IDEOGRAPHS (\w+)""")
    }
}
