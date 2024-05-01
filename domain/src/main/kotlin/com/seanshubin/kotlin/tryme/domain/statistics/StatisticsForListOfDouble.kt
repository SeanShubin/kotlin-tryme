package com.seanshubin.kotlin.tryme.domain.statistics

object StatisticsForListOfDouble {
    fun List<Double>.median():Double {
        val sizeEven = size % 2 ==0
        return if(sizeEven){
            (get(size/2-1) + get(size/2))/2
        } else {
            get(size/2)
        }
    }

    fun List<Double>.mean():Double {
        return sum() / size
    }

    fun List<Double>.deviations():List<Double> {
        val mean = mean()
        return map { it - mean }
    }

    fun List<Double>.squaredDeviations():List<Double> {
        return deviations().map{ it * it }
    }

    fun List<Double>.variance():Double {
        return squaredDeviations().mean()
    }

    fun List<Double>.standardDeviation():Double {
        return Math.sqrt(variance())
    }
}