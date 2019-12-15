package com.seanshubin.kotlin.tryme.domain.calculator.arithmetic

import com.seanshubin.kotlin.tryme.domain.parser.MatchException
import kotlin.test.Test
import kotlin.test.assertEquals

class CalculatorTest {
    @Test
    fun number() {
        // given
        val input = "123"

        // when
        val actual = CalculatorExpressionAssemblers.eval(input)

        // then
        assertEquals(123, actual)
    }

    @Test
    fun addition() {
        // given
        val input = "10 + 5"

        // when
        val actual = CalculatorExpressionAssemblers.eval(input)

        // then
        assertEquals(15, actual)
    }

    @Test
    fun longChain() {
        // given
        val input = "1 + 2 + 3 + 4"

        // when
        val actual = CalculatorExpressionAssemblers.eval(input)

        // then
        assertEquals(10, actual)
    }

    @Test
    fun subtraction() {
        // given
        val input = "10 - 5"

        // when
        val actual = CalculatorExpressionAssemblers.eval(input)

        // then
        assertEquals(5, actual)
    }

    @Test
    fun multiplication() {
        // given
        val input = "2 * 3"

        // when
        val actual = CalculatorExpressionAssemblers.eval(input)

        // then
        assertEquals(6, actual)
    }

    @Test
    fun division() {
        // given
        val input = "6 / 2"

        // when
        val actual = CalculatorExpressionAssemblers.eval(input)

        // then
        assertEquals(3, actual)
    }

    @Test
    fun subtractionLeftToRight() {
        // given
        val input = "3 - 2 - 1"

        // when
        val actual = CalculatorExpressionAssemblers.eval(input)

        // then
        assertEquals(0, actual)
    }

    @Test
    fun divisionLeftToRight() {
        // given
        val input = "48 / 6 / 2"

        // when
        val actual = CalculatorExpressionAssemblers.eval(input)

        // then
        assertEquals(4, actual)
    }

    @Test
    fun subtractionParenthesis() {
        // given
        val input = "3 - (2 - 1)"

        // when
        val actual = CalculatorExpressionAssemblers.eval(input)

        // then
        assertEquals(2, actual)
    }

    @Test
    fun divisionParenthesis() {
        // given
        val input = "48 / (6 / 2)"

        // when
        val actual = CalculatorExpressionAssemblers.eval(input)

        // then
        assertEquals(16, actual)
    }

    @Test
    fun multiplyBeforeAdd() {
        // given
        val input = "1 + 2 * 3"

        // when
        val actual = CalculatorExpressionAssemblers.eval(input)

        // then
        assertEquals(7, actual)
    }

    @Test
    fun unsupportedOperator() {
        // given
        val input = "1 + 2 % 3 * 4"

        // when
        val exception = try {
            CalculatorExpressionAssemblers.eval(input)
            throw RuntimeException("unexpected")
        } catch (ex: MatchException) {
            ex
        }

        // then
        assertEquals(
            "[1:7] Expected one of: number, plus, minus, times, divide, open-paren, close-paren, whitespace-block",
            exception.message
        )
    }
}
