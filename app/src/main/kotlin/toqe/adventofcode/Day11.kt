package toqe.adventofcode

class Day11 {
    val blacklist = arrayOf('i', 'o', 'l')

    fun run() {
        var password = InputFileHelper.readInput(11)
        var passwordChars = password.toCharArray()

        while (!isSecurePassword(passwordChars)) {
            inc(passwordChars)
        }

        println("$password -> ${String(passwordChars)}")

        inc(passwordChars)

        while (!isSecurePassword(passwordChars)) {
            inc(passwordChars)
        }

        println(" -> ${String(passwordChars)}")
    }

    fun inc(password: CharArray): CharArray {
        for (i in 0..password.size - 1) {
            val index = password.size - 1 - i

            if (password[index] < 'z') {
                password[index]++

                if (blacklist.contains(password[index])) {
                    password[index]++
                }

                break
            }

            password[index] = 'a'
        }

        return password
    }

    fun isSecurePassword(password: CharArray): Boolean {
        var containsIncreasingStraight = false
        var firstPairIndex = -1
        var secondPairIndex = -1

        for (i in 0..password.size - 1) {
            if (blacklist.contains(password[i])) {
                return false
            }

            if (!containsIncreasingStraight && i < password.size - 2 && (password[i] + 1) == password[i + 1] && (password[i + 1] + 1) == password[i + 2]) {
                containsIncreasingStraight = true
            }

            if (secondPairIndex < 0 && i < password.size - 1 && password[i] == password[i + 1]) {
                if (firstPairIndex < 0) {
                    firstPairIndex = i
                } else if (i - firstPairIndex > 1) {
                    secondPairIndex = i
                }
            }
        }

        return containsIncreasingStraight && secondPairIndex > 0
    }
}