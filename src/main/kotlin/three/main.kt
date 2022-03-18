package three

import java.io.File
import java.math.BigInteger



fun main() {
    val input = File("src/main/kotlin/three/input.txt").readLines()
    println(TobogganTrajectory(TreePattern.fromInputList(input)).travelToBottom())

    val values =
        listOf(
        Pair(1, 1),
        Pair(3, 1),
        Pair(5, 1),
        Pair(7, 1),
        Pair(1, 2)
    ).map { TobogganTrajectory(TreePattern.fromInputList(input)).travelToBottom(it.first, it.second) }
    println(values)
    println(values.fold (BigInteger.ONE) {  sum, element -> sum.multiply(element.toBigInteger()) })
}

data class TreePattern(val treeMatrix: List<List<Boolean>>) {
    companion object Builder {
        fun fromInputList(inputList: List<String>): TreePattern {
            require(inputList.isNotEmpty() && inputList.none { it == "" })

            return TreePattern(inputList.map { it.map { char ->
                when(char) {
                    '.' -> false
                    '#' -> true
                    else -> {print(char); throw IllegalArgumentException()}
                }
            }})
        }
    }
}

class TobogganTrajectory(private val treePattern: TreePattern) {
    private var currRow = 0
    private var currCol = 0

    val isAtTree: Boolean
        get() = treePattern.treeMatrix[currRow][currCol]

    fun isAtBottom() = currRow >= treePattern.treeMatrix.size - 1

    fun move(dr: Int, dc: Int) {
        currRow = (treePattern.treeMatrix.size - 1).coerceAtMost(currRow + dr)
        currCol = (currCol + dc) % treePattern.treeMatrix[0].size
    }

    // Returns number of trees found in decent to bottom
    fun travelToBottom(right: Int = 3, left: Int = 1): Int {
        var numTrees = 0
        while(!isAtBottom()) {
            move(left, right)
            numTrees += if(isAtTree) 1 else 0
        }
        return numTrees
    }
}


