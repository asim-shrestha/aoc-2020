package three

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec

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
        it("Exception if pattern contains non '.', '#' characters") {
            shouldThrow<IllegalArgumentException> {
                TreePattern.fromInputList(listOf("..#123."))
            }
        }

        it("Succeeds with sample input") {
            TreePattern.fromInputList(sampleTreePattern)
        }
    }
})
