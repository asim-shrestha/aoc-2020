package two

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.describeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.int
import io.kotest.property.exhaustive.azstring
import io.kotest.property.forAll

class MainTest : DescribeSpec({
    include(createPasswordTest(::createRangePasswordFromRow))
    describe("Create range password instance") {
        it("Exception if low is greater than high") {
            shouldThrowAny { createRangePasswordFromRow("5-3 a: abcde") }
        }
    }

    include(createPasswordTest(::createPositionPasswordFromRow))
    describe("Create position password instance") {
        it("Exception if positions are less than 1") {
            forAll(Arb.int(-1000..0), Arb.int(-1000..0)) { pos1, pos2 ->
                shouldThrowAny {
                    createPositionPasswordFromRow("$pos1-$pos2 a: abcde")
                }
                true
            }
        }
    }

    describe("Range password instance validity") {
        it("Always fails if both requirements 0 and letter in string") {
            forAll<Char, String> { letter, password ->
                !createRangePasswordFromRow("0-0 $letter: $letter${password.filter { it.isLetterOrDigit() }}").isValid()
            }
        }

        it("Succeeds if count is in range") {
            val max = 100
            forAll(Arb.int(1 until max), Arb.char()) {size, letter ->
                createRangePasswordFromRow("1-$max $letter: ${letter.toString().repeat(size)}").isValid()
            }
        }

        it("Fails if count is out of range") {
            val max = 100
            forAll(Arb.int(1 until max), Arb.char()) {size, letter ->
                !createRangePasswordFromRow("0-0 $letter: ${letter.toString().repeat(size)}").isValid()
            }
        }

        it("Succeeds with example row") {
            withClue("Password has 1 'a' so it is within range") {
                createRangePasswordFromRow("1-3 a: abcde").isValid() shouldBe true
            }
        }
    }

    describe("Position password instance validity") {
        it("Always invalid with same first and second position") {
            val maxLen = 10
            forAll(Arb.int(1..maxLen), Arb.char(), Exhaustive.azstring(maxLen..maxLen)) { pos, char, string ->
                !createPositionPasswordFromRow("$pos-$pos $char: $string").isValid()
            }
        }

        it("Valid with only one position containing letter") {
            withClue("Position 1 contains a and position 3 does not.") {
                createPositionPasswordFromRow("1-3 a: abcde").isValid() shouldBe true
            }
        }

        it("Invalid with no positions containing letter") {
            withClue("neither position 1 nor position 3 contains b.") {
                createPositionPasswordFromRow("1-3 b: cdefg").isValid() shouldBe false
            }
        }

        it("Invalid with both positions containing letter") {
            withClue("both position 2 and position 9 contain c.") {
                createPositionPasswordFromRow("2-9 a: ccccccccc").isValid() shouldBe false
            }
        }
    }
})

fun createPasswordTest(createPasswordFromRow: (row: String) -> PasswordInstance) = describeSpec {
    describe("Basic create password test for ${createPasswordFromRow.toString()} ") {
        it("Exception with badly formatted rows") {
            listOf(
                "1-3 a:abcde",
                "1-3 a: ",
                "1-3 a abcde",
                "-3 a: abcde",
                "1- a: abcde",
                "a",
                "",
                "ASDASD").forEach {
                shouldThrowAny { createRangePasswordFromRow(it) }
            }
        }

        it("Exception on negative low values") {
            shouldThrowAny { createRangePasswordFromRow("-1-3 a: abcde") }
        }

        it("Succeeds with example row") {
            val expected = PasswordInstance(RangePasswordPolicy(1, 3, 'a'), "abcde")
            expected shouldBe createRangePasswordFromRow("1-3 a: abcde")
        }
    }
}
