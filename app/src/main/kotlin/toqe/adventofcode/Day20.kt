package toqe.adventofcode

class Day20 {
    fun run() {
        val targetPresents = Integer.parseInt(InputFileHelper.readInput(20).trim())

        // part 1
        val giftsPerHousePart1 = Array<Int>(1000000) { 1 }

        for (i in 1..giftsPerHousePart1.size) {
            var c = 1

            while(true) {
                val index = i * c - 1

                if (index < giftsPerHousePart1.size) {
                    giftsPerHousePart1[index] += 10 * i
                } else {
                    break
                }

                c++
            }
        }

        val answerPart1 = giftsPerHousePart1.withIndex().first { it.value >= targetPresents }.index + 1
        println("part 1 house number $answerPart1")

        // part 2
        val giftsPerHousePart2 = Array<Int>(1000000) { 0 }

        for (i in 1..giftsPerHousePart2.size) {
            for (c in 1..50) {
                val index = i * c - 1

                if (index < giftsPerHousePart2.size) {
                    giftsPerHousePart2[index] += 11 * i
                }
            }
        }

        val answerPart2 = giftsPerHousePart2.withIndex().first { it.value >= targetPresents }.index + 1
        println("part 2 house number $answerPart2")
    }
}