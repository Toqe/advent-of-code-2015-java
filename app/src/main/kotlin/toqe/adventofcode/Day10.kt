package toqe.adventofcode

class Day10 {
    fun run() {
        val exampleInput = "1"
        val exampleResult = lookAndSay(exampleInput, 4)
        println("example result is ${exampleResult.length} long: $exampleResult")

        val input = InputFileHelper.readInput(10)

        // step 1
        val step1Result = lookAndSay(input, 50)
        println("result is ${step1Result.length} long")

        // step2
        val step2Result = lookAndSay(input, 50)
        println("result is ${step2Result.length} long")
    }

    fun lookAndSay(input: String, todoRounds: Int): String {
        if (todoRounds <= 0) {
            return input
        }

        val result = StringBuilder()
        var lastChar: Char? = null
        var lastCharCount = 0

        for (i in 0..input.length - 1) {
            if (input[i] != lastChar) {
                if (lastChar != null) {
                    result.append(lastCharCount)
                    result.append(lastChar)
                }

                lastCharCount = 1
                lastChar = input[i]
            } else {
                lastCharCount++
            }
        }

        if (lastChar != null) {
            result.append(lastCharCount)
            result.append(lastChar)
        }

        return lookAndSay(result.toString(), todoRounds - 1)
    }
}