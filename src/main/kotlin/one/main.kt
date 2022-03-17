package one

import arrow.core.*

import java.io.File

fun main() {
    val input = File("src/main/kotlin/one/input.txt").readLines().map(String::toInt)
    val desiredSum = 2020

    // Part 1
    val pair = twoSum(input, desiredSum)
    if(pair.isRight()) {
        println(pair.map { it.first * it.second }.getOrElse { throw IllegalArgumentException() })
    }

    // Part 2
    val triple = threeSum(input, desiredSum)
    if(triple.isRight()) {
        println(triple.map { it.first * it.second * it.third }.getOrElse { throw IllegalArgumentException() })
    }
}

fun twoSum(input: List<Int>, desiredSum: Int): Either<IllegalArgumentException, Pair<Int, Int>> {
    val complement = input.firstNotNullOfOrNull { input.minus(it).toSet().firstOrNull{curr -> input.contains(desiredSum - curr)}}
    return if(complement == null) Either.Left(IllegalArgumentException()) else Either.Right(Pair(desiredSum - complement, complement))
}

fun threeSum(input: List<Int>, desiredSum: Int): Either<IllegalArgumentException, Triple<Int, Int, Int>> {
    input.forEach{
        when(val eitherPair = twoSum(input.minus(it), desiredSum - it)) {
            is Either.Right -> {
                val pair = eitherPair.orNull()
                return Either.Right(Triple(it, pair?.first ?: 0, pair?.second ?: 0))
            }
            else -> {}
        }
    }
    return Either.Left(IllegalArgumentException())
}
