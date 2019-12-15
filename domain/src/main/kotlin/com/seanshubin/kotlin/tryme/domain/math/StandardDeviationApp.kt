package com.seanshubin.kotlin.tryme.domain.math

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode

val TWO: BigDecimal = BigDecimal.valueOf(2)

fun main(args: Array<String>) {
    val numbers = listOf(5, 8, 5).map { BigDecimal(it) }
    val scale = 1000
    val actual = numbers.standardDeviation(scale)
    val expected = BigDecimal(
        "1.4142135623730950488016887242096980785696718753769480731766797379907324784621070388503875343276415727350138462309122970249248360558507372126441214970999358314132226659275055927557999505011527820605714701095599716059702745345968620147285174186408891986095523292304843087143214508397626036279952514079896872533965463318088296406206152583523950547457502877599617298355752203375318570113543746034084988471603868999706990048150305440277903164542478230684929369186215805784631115966687130130156185689872372352885092648612494977154218334204285686060146824720771435854874155657069677653720226485447015858801620758474922657226002085584466521458398893944370926591800311388246468157082630100594858704003186480342194897278290641045072636881313739855256117322040245091227700226941127573627280495738108967504018369868368450725799364729060762996941380475654823728997180326802474420629269124859052181004459842150591120249441341728531478105803603371077309182869314710171111683916581726889419758716582152128229518488472"
    )
    if (actual == expected) {
        println("SUCCESS")
    } else {
        println("FAILURE")
    }
    val bigValue =
        BigDecimal("357634895695089724523985729384759203749045673498567349056793048753957239456327456495867934856790345670945867345964390879285732948562349052304759823475092346729847609457609548238457635763489569508972452398572938475920374904567349856734905679304875395723945632745649586793485679034567094586734596439087928573294856234905230475982347509234672984760945760954823845763576348956950897245239857293847592037490456734985673490567930487539572394563274564958679348567903456709458673459643908792857329485623490523047598234750923467298476094576095482384576")
    println(squareRoot(bigValue, 1000))
}

private fun List<BigDecimal>.standardDeviation(scale: Int): BigDecimal {
    return squareRoot(this.variance(), scale)
}

private fun List<BigDecimal>.average(): BigDecimal =
    sum() / BigDecimal.valueOf(size.toLong())

private fun List<BigDecimal>.sum(): BigDecimal {
    var accumulator = ZERO
    for (item in this) {
        accumulator += item
    }
    return accumulator
}

private fun List<BigDecimal>.variance(): BigDecimal {
    val average = this.average()
    val subtractFromMean = { a: BigDecimal -> a - average }
    val square = { a: BigDecimal -> a * a }
    return this.map(subtractFromMean).map(square).average()
}

private fun squareRoot(x: BigDecimal, scale: Int): BigDecimal {
    var lastComputed = ZERO
    val estimateAsDouble = Math.sqrt(x.toDouble())
    var currentEstimate = when {
        estimateAsDouble.isFinite() -> BigDecimal(estimateAsDouble)
        estimateAsDouble == Double.POSITIVE_INFINITY -> BigDecimal(Double.MAX_VALUE)
        estimateAsDouble == Double.NEGATIVE_INFINITY -> BigDecimal(Double.MIN_VALUE)
        else -> BigDecimal.ONE
    }
    while (lastComputed != currentEstimate) {
        lastComputed = currentEstimate
        currentEstimate = x.divide(lastComputed, scale, RoundingMode.HALF_UP)
        currentEstimate = currentEstimate.add(lastComputed)
        currentEstimate = currentEstimate.divide(TWO, scale, RoundingMode.HALF_UP)
    }
    return currentEstimate
}
