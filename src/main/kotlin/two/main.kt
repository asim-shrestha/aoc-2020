package two

import java.io.File

fun main() {
    val input = File("src/main/kotlin/two/input.txt").readLines()

    println(input.map { createRangePasswordFromRow(it).isValid() }.count { it })
    println(input.map { createPositionPasswordFromRow(it).isValid() }.count { it })
}

fun createRangePasswordFromRow(row: String): PasswordInstance {
    val values = getRowMatchValues(row)
    val policy = RangePasswordPolicy(values[1].toInt(), values[2].toInt(), values[3].first())
    return PasswordInstance(policy, values[4])
}

fun createPositionPasswordFromRow(row: String): PasswordInstance {
    val values = getRowMatchValues(row)
    val policy = PositionPasswordPolicy(values[1].toInt(), values[2].toInt(), values[3].first())
    return PasswordInstance(policy, values[4])
}

fun getRowMatchValues(row: String): List<String> {
    val match = "^(\\d+)-(\\d+) (.): (.+)$".toRegex().find(row)
    require( match?.groupValues?.size == 5)
    return match!!.groupValues
}

interface PasswordPolicy {
    fun isValid(password: String): Boolean
}

data class RangePasswordPolicy(val low: Int, val high: Int, val char: Char): PasswordPolicy {
    init {
        if (low > high || low < 0 || high < 0) throw IllegalArgumentException()
    }

    override fun isValid(password: String): Boolean {
        return password.count{it == char} in (low..high)
    }
}

data class PositionPasswordPolicy(val first: Int, val second: Int, val char: Char): PasswordPolicy {
    init {
        if(first < 0 || second < 0) throw IllegalArgumentException()
    }

    override fun isValid(password: String): Boolean {
        // Subtract 1 because policy is 1-indexed
        return listOf(password[first - 1], password[second - 1]).count{ it == char } == 1
    }
}

data class PasswordInstance(val policy: PasswordPolicy, val password: String) {
    fun isValid(): Boolean {
        return policy.isValid(password)
    }
}