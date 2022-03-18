package four

import java.io.File


val requiredFields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid" /*"cid"*/)

fun main() {
    val input = File("src/main/kotlin/four/input.txt").readText()

    // Part 1
    println(input.splitByPassportStrings().map(String::convertToPassportMap).count(::isPassportMapValid))

}

fun String.splitByPassportStrings() = this.split("\n\n").map { str -> str.replace("\n", " ") }

fun String.convertToPassportMap(): Map<String, String> {
    return this.split(          " ")
        .mapNotNull { "^(.{3}):(.*)$".toRegex().find(it) }
        .associate { it.groupValues[1] to it.groupValues[2] }
}

fun isPassportMapValid(passport: Map<String, String>): Boolean {
    return passport.keys.containsAll(requiredFields)
}

fun isPassportMapValid2(passport: Map<String, String>): Boolean {
    TODO()
//    return requiredFields.all {  }
}