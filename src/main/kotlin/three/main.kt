package three

import java.io.File

fun main() {
    val input = File("src/main/kotlin/two/input.txt").readLines()
}

data class TreePattern(val treeMatrix: List<List<Boolean>>) {
    companion object Builder {
        fun fromInputList(inputList: List<String>): TreePattern {
            return TreePattern(inputList.map { it.map { char ->
                when(char) {
                    '.' -> false
                    '#' -> true
                    else -> throw IllegalArgumentException()
                }
            }})
        }
    }
}

class TobbaganTrajectory(private val treePattern: TreePattern) {
    private var currRow = 0
    private var currCol = 0

    val isAtTree: Boolean
        get() = treePattern.treeMatrix[0][0]

    fun hasReachedBottom() = currRow >= treePattern.treeMatrix.size

    fun move(dr: Int, dc: Int) {
        currRow = (treePattern.treeMatrix.size - 1).coerceAtMost(currRow + dr)
        currCol = (treePattern.treeMatrix[0].size + dc) % treePattern.treeMatrix[0].size
    }
}


