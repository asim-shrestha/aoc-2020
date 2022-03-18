package four

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.maps.shouldContainValue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.property.forAll

class MainTest : DescribeSpec({
    val sampleInput = """
        ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
        byr:1937 iyr:2017 cid:147 hgt:183cm

        iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
        hcl:#cfa07d byr:1929

        hcl:#ae17e1 iyr:2013
        eyr:2024
        ecl:brn pid:760753108 byr:1931
        hgt:179cm

        hcl:#cfa07d eyr:2025 pid:166559648
        iyr:2011 ecl:brn hgt:59in
    """
    describe("Split password strings") {
        it("4 passwords in sample input") {
            val result = sampleInput.splitByPassportStrings()
            result.size shouldBe 4
        }

        it("Correct first password string in sample input") {
            val result = sampleInput.splitByPassportStrings()
            result[0] shouldContain "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd"
        }
    }

    describe("Convert to map test") {
        it("Conversion succeeds with 3 letter key and mapped value") {
            "hcl:#ae17e1".convertToPassportMap() shouldContainKey "hcl"
            "hcl:#ae17e1".convertToPassportMap() shouldContainValue "#ae17e1"
        }
        it("Conversion succeeds with multiple valid values") {
            val res = "hcl:#ae17e1 abc:123".convertToPassportMap()
            res shouldBe mapOf("hcl" to "#ae17e1", "abc" to "123")
        }
        it("Conversion fails with empty input") {
            "".convertToPassportMap().size shouldBe 0
        }
        it("Conversion fails with non three letter key") {
            "four:123".convertToPassportMap().size shouldBe 0
        }
        it("Conversion fails with non map") {
            "four123".convertToPassportMap().size shouldBe 0
        }
        it("Map only contains passport keys") {
            val passportMap = "hcl:#ae17e1".convertToPassportMap()
            forAll<String> { str ->
                if(str == "hcl") true else !passportMap.containsKey(str)
            }
        }
    }

    describe("Is valid passport") {
        it("Valid passport is valid") {
            isPassportMapValid("""ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm""".convertToPassportMap()) shouldBe true
        }

        it("Passport only missing cid is valid") {
            isPassportMapValid("""hcl:#ae17e1 iyr:2013 eyr:2024 ecl:brn pid:760753108 byr:1931 hgt:179cm""".convertToPassportMap()) shouldBe true
        }

        it("Passport missing hgt is invalid") {
            isPassportMapValid("""iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884 hcl:#cfa07d byr:1929""".convertToPassportMap()) shouldBe false
        }

        it("Passport missing cid and hgt is invalid") {
            isPassportMapValid("""iyr:2013 ecl:amb eyr:2023 pid:028048884 hcl:#cfa07d byr:1929""".convertToPassportMap()) shouldBe false
        }

        it("Empty passport invalid") {
            isPassportMapValid("".convertToPassportMap()) shouldBe false
        }

        it("Sample contains 4 valid passports") {
            sampleInput.splitByPassportStrings().map { it.convertToPassportMap() }.count(::isPassportMapValid) shouldBe 2
        }
    }
})
