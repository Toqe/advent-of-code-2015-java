package toqe.adventofcode

class Day12 {
    fun run() {
        val input = InputFileHelper.readInput(12)

        // part 1
        var sum = 0L

        for (match in "-?\\d+".toRegex().findAll(input)) {
            sum += Integer.parseInt(match.value)
        }

        println("sum: $sum")

        // part 2
        val parser = Parser(input)
        val jsonNode = parser.parse()
        val part2Sum = jsonNode.sum()
        println("part 2 sum: $part2Sum")
    }

    class Parser(val input: String) {
        var index = 0

        fun parse(): JsonNode {
            return when (input[index]) {
                '\"' -> parseString()
                '[' -> parseArray()
                '{' -> parseObject()
                else -> if (Character.isDigit(input[index]) || input[index] == '-') parseNumber() else throw Exception("unknown JSON character ${input[index]}")
            }
        }

        fun parseNumber(): JsonNode {
            val startIndex = index

            if (input[index] == '-') {
                index++
            }

            while (Character.isDigit(input[index])) {
                index++
            }

            val endIndex = index
            val result = JsonNode()
            result.value = Integer.parseInt(input.substring(startIndex, endIndex))
            return result
        }

        fun parseString(): JsonNode {
            val result = JsonNode()
            index++
            val startIndex = index

            while (input[index] != '\"') {
                index++
            }

            val endIndex = index

            if (input.substring(startIndex, endIndex) == "red") {
                result.isRed = true
            }

            index++

            return result
        }

        fun parseArray(): JsonNode {
            val result = JsonNode()
            result.isArray = true
            index++

            while (true) {
                val child = parse()
                result.children.add(child)

                if (input[index] == ']') {
                    index++
                    return result
                } else {
                    index++
                }
            }
        }

        fun parseObject(): JsonNode {
            val result = JsonNode()
            index++

            while (true) {
                parse()
                index++ // ":"
                val child = parse()
                result.children.add(child)

                if (input[index] == '}') {
                    index++
                    return result
                } else {
                    index++
                }
            }
        }
    }

    class JsonNode {
        var value = 0
        var isRed = false
        var isArray = false
        var children = ArrayList<JsonNode>()

        fun sum(): Long = value + if (!isArray && children.any { it.isRed }) 0 else children.sumOf { it.sum() }
    }
}