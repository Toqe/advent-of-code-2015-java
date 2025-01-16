package toqe.adventofcode

class Day16 {
    fun run() {
        val targetMap = mapOf(
            "children" to 3, "cats" to 7, "samoyeds" to 2, "pomeranians" to 3, "akitas" to 0, "vizslas" to 0,
            "goldfish" to 5, "trees" to 3, "cars" to 2, "perfumes" to 1
        )

        val input = InputFileHelper.readInput(16)
        val data = parse(input)

        val sueNumberPart1 = findSueNumer(targetMap, data, Mode.Part1)
        println("Sue $sueNumberPart1 is a match for part 1")

        val sueNumberPart2 = findSueNumer(targetMap, data, Mode.Part2)
        println("Sue $sueNumberPart2 is a match for part 2")
    }

    fun parse(input: String): Array<HashMap<String, Int>> {
        val lines = input.trim().split('\n')
        val result = Array<HashMap<String, Int>>(lines.size) { HashMap<String, Int>() }

        for (i in 0..lines.size - 1) {
            val line = lines[i]
            val firstDoublepointIndex = line.indexOf(':')
            val compounds = line.substring(firstDoublepointIndex + 2).split(", ")

            for (compound in compounds) {
                val compoundSplitted = compound.split(": ")
                val compoundName = compoundSplitted[0]
                val compoundNumber = Integer.parseInt(compoundSplitted[1])
                val mapEntry = result[i]
                mapEntry.put(compoundName, compoundNumber)
            }
        }

        return result
    }

    fun findSueNumer(targetMap: Map<String, Int>, data: Array<HashMap<String, Int>>, mode: Mode): Int {
        val readingIsLessForKeys = arrayOf("cats", "trees")
        val readingIsMoreForKeys = arrayOf("pomeranians", "goldfish")
        var index = 0

        for (entry in data) {
            index++
            var match = true

            for ((k, v) in entry) {
                if (mode == Mode.Part1) {
                    if (targetMap[k] != v) {
                        match = false
                    }
                } else {
                    if ((k in readingIsLessForKeys && targetMap[k]!! >= v)
                        || (k in readingIsMoreForKeys && targetMap[k]!! <= v)
                        || (k !in readingIsLessForKeys && k !in readingIsMoreForKeys && targetMap[k] != v)
                    ) {
                        match = false
                    }
                }
            }

            if (match) {
                return index
            }
        }

        throw Exception("Sue not found")
    }

    enum class Mode {
        Part1,
        Part2,
    }
}