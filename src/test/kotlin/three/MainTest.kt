package three

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class MainTest : DescribeSpec({
    val sampleTreePattern = listOf(
        "..##.......",
        "#...#...#..",
        ".#....#..#.",
        "..#.#...#.#",
        ".#...##..#.",
        "..#.##.....",
        ".#.#.#....#",
        ".#........#",
        "#.##...#...",
        "#...##....#",
        ".#..#...#.#"
    )

    describe("Generate tree pattern") {
        it("Fails on empty input") {
            listOf(listOf(), listOf("")).forEach{
                shouldThrow<IllegalArgumentException> {
                    TreePattern.fromInputList(it)
                }
            }
        }
        it("Exception if pattern contains non '.', '#' characters") {
            shouldThrow<IllegalArgumentException> {
                TreePattern.fromInputList(listOf("..#123."))
            }
        }

        it("Succeeds with sample input") {
            TreePattern.fromInputList(sampleTreePattern)
        }
    }

    describe("Basic is at Tree") {
        it("Tree only pattern is at tree") {
            TobogganTrajectory(TreePattern.fromInputList(listOf("#"))).isAtTree shouldBe true
        }

        it("Snow only pattern is NOT at tree") {
            TobogganTrajectory(TreePattern.fromInputList(listOf("."))).isAtTree shouldBe false
        }
    }

    describe("Travel to bottom") {
        it("7 trees in first sample input decent") {
           val tt = TobogganTrajectory(TreePattern.fromInputList(sampleTreePattern))
            tt.travelToBottom() shouldBe 7
            tt.isAtBottom() shouldBe true
        }

        it("right 1 slope test") {
            val tt = TobogganTrajectory(TreePattern.fromInputList(sampleTreePattern))
            tt.travelToBottom(1) shouldBe 2
            tt.isAtBottom() shouldBe true
        }

        it("right 3 slope test") {
            val tt = TobogganTrajectory(TreePattern.fromInputList(sampleTreePattern))
            tt.travelToBottom(3) shouldBe 7
            tt.isAtBottom() shouldBe true
        }

        it("right 5 slope test") {
            val tt = TobogganTrajectory(TreePattern.fromInputList(sampleTreePattern))
            tt.travelToBottom(5) shouldBe 3
            tt.isAtBottom() shouldBe true
        }

        it("right 7 slope test") {
            val tt = TobogganTrajectory(TreePattern.fromInputList(sampleTreePattern))
            tt.travelToBottom(7) shouldBe 4
            tt.isAtBottom() shouldBe true
        }

        it("right 1 down 2 slope test") {
            val tt = TobogganTrajectory(TreePattern.fromInputList(sampleTreePattern))
            tt.travelToBottom(1,2) shouldBe 2
            tt.isAtBottom() shouldBe true
        }
    }
})
