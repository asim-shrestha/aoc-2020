package one

import arrow.core.Either
import arrow.core.merge
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.forAll

class MainTest : DescribeSpec({
    describe("Find pair") {
        it("Exception if list is too small") {
            forAll(Arb.list(Arb.int(1..1), 0..1)) { inputList ->
                twoSum(inputList, 2).merge() is IllegalArgumentException
            }
        }

        it("Exception if no pair exists") {
            forAll(Arb.list(Arb.int(1..1000), 0..100)) { inputList ->
                twoSum(inputList, -1).merge() is IllegalArgumentException
            }
        }

        it("Finds the first pair") {
            twoSum(listOf(1, 2, 0, 3), 3).shouldBe(Either.Right(Pair(1, 2)))
        }

        it("Passes basic test") {
            twoSum(listOf(1721, 979, 366, 299, 675, 1456), 2020).shouldBe(Either.Right(Pair(1721, 299)))
        }
    }

    describe("Find triple") {
        it("Exception if list is too small") {
            forAll(Arb.list(Arb.int(1..1), 0..2)) { inputList ->
                threeSum(inputList, 3).merge() is IllegalArgumentException
            }
        }

        it("Exception if no triple") {
            forAll(Arb.list(Arb.int(1..1000), 0..50)) { inputList ->
                threeSum(inputList, -1).merge() is IllegalArgumentException
            }
        }

        it("Finds the first triple") {
            threeSum(listOf(1, 2, 3, 0, 0, 6), 6).shouldBe(Either.Right(Triple(1, 2, 3)))
        }

        it("Passes basic test") {
            threeSum(listOf(1721, 979, 366, 299, 675, 1456), 2020).shouldBe(Either.Right(Triple(979, 366, 675)))
        }
    }
})