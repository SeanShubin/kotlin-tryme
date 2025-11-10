package com.seanshubin.kotlin.tryme.domain.compare

object CharacterPrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
        var alphaCount = 0
        var digitCount = 0
        var otherCount = 0
        val digitCharacter = mutableMapOf<Character.UnicodeBlock, List<Char>>()
        // Get all predefined Unicode blocks from the JVM
        val unicodeBlocks = Character.UnicodeBlock::class.java.declaredFields
            .filter { it.type == Character.UnicodeBlock::class.java }
            .mapNotNull { field ->
                field.isAccessible = true
                field.get(null) as? Character.UnicodeBlock
            }
            .distinct()
        
        println("Found ${unicodeBlocks.size} Unicode blocks")
        println()
        
        // Outer loop: iterate over each Unicode block
        for (block in unicodeBlocks) {
            println("Processing block: $block")
            val digitCharactersInBlock = mutableListOf<Char>()
            // Inner loop: iterate over all possible code points to find those in this block
            var count = 0
            for (codePoint in 0..Character.MAX_CODE_POINT) {
                if (Character.UnicodeBlock.of(codePoint) == block) {
                    val character = codePoint.toChar()
                    if(character.isDigit() && character.isLetter()){
                        throw RuntimeException("Both digit and letter at index $count in block $block")
                    } else if(character.isLetter()) {
                        alphaCount++
                    } else if(character.isDigit()){
                        digitCount++
                        digitCharactersInBlock.add(character)
                    } else {
                        otherCount++
                    }
                    // Do something with each character
                    if (count < 5) {
                        println("  U+${codePoint.toString(16).uppercase().padStart(4, '0')}: '${String(Character.toChars(codePoint))}'")
                    }
                    count++
                }
            }
            digitCharacter[block] = digitCharactersInBlock
            println("  Total characters in block: $count")
            println()
        }

        println("Total alpha characters: $alphaCount")
        println("Total digit characters: $digitCount")
        println("Total other characters: $otherCount")
        digitCharacter.forEach { (block, characters) ->
            val blockName = block.toString()
            val charactersAsString = characters.joinToString(" " )

            println("$blockName(${characters.size}): $charactersAsString")
            println()
        }
    }
}