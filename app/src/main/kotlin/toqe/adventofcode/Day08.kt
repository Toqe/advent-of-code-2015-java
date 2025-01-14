package toqe.adventofcode

class Day08 {
    fun run() {
        val example = """
            ""
            "abc"
            "aaa\"aaa"
            "\x27"
        """.trimIndent()

        val input = InputFileHelper.readInput(8)

        val exampleDecodeResult = decodeAllLines(example)
        println("result for example: $exampleDecodeResult")

        val inputDecodeResult = decodeAllLines(input)
        println("result for input: $inputDecodeResult")

        // part 2
        val exampleEncodeResult = encodeAllLines(example)
        println("result for example: $exampleEncodeResult")

        val inputEncodeResult = encodeAllLines(input)
        println("result for input: $inputEncodeResult")
    }

    fun decodeAllLines(input: String): Long {
        var sum = 0L

        for (line in input.split("\n")) {
            if (line == "") {
                continue
            }

            val codeLength = line.length
            val decoded = decode(line)
            val decodedLength = decoded.length
            sum += codeLength - decodedLength
        }

        return sum
    }

    fun decode(input: String): String {
        var trimmedInput = input.substring(1, input.length - 1)
        val sb = StringBuilder(input.length)
        var i = 0

        while (i < trimmedInput.length) {
            if (i < trimmedInput.length - 1 && trimmedInput[i] == '\\' && trimmedInput[i + 1] == '\\') {
                sb.append("\\")
                i++
            } else if (i < trimmedInput.length - 1 && trimmedInput[i] == '\\' && trimmedInput[i + 1] == '\"') {
                sb.append("\"")
                i++
            } else if (i < trimmedInput.length - 3
                && trimmedInput[i] == '\\'
                && trimmedInput[i + 1] == 'x'
                && Character.isLetterOrDigit(trimmedInput[i + 2])
                && Character.isLetterOrDigit(trimmedInput[i + 3])
            ) {
                sb.append("a")
                i += 3
            } else {
                sb.append(trimmedInput[i])
            }

            i++
        }

        val result = sb.toString()
        return result
    }

    fun encodeAllLines(input: String): Long {
        var sum = 0L

        for (line in input.split("\n")) {
            if (line == "") {
                continue
            }

            val codeLength = line.length
            val encoded = encode(line)
            val encodedLength = encoded.length
            sum += encodedLength - codeLength
        }

        return sum
    }

    fun encode(input: String): String {
        val sb = StringBuilder(input.length)

        for (i in 0..input.length - 1) {
            sb.append(
                when (input[i]) {
                    '"' -> "\\\""
                    '\\' -> "\\\\"
                    else -> input[i]
                }
            )
        }

        val result = "\"" + sb.toString() + "\""
        return result
    }
}