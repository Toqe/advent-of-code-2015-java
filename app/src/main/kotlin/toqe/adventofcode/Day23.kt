package toqe.adventofcode

class Day23 {
    fun run() {
        val input = InputFileHelper.readInput(23)
        val instructions = input.trim().split('\n')
//        var registerA = 0L
        var registerA = 1L
        var registerB = 0L
        var instructionIndex = 0

        while (true) {
            if (instructionIndex < 0 || instructionIndex >= instructions.size) {
                break
            }

            val line = instructions[instructionIndex]
            val instruction = line.substring(0, 3)

//            println("($registerA, $registerB) $instructionIndex : $line")

            when (instruction) {
                "hlf" -> {
                    registerA = if (line[4] == 'a') registerA / 2 else registerA
                    registerB = if (line[4] == 'b') registerB / 2 else registerB
                    instructionIndex++
                }

                "tpl" -> {
                    registerA = if (line[4] == 'a') registerA * 3 else registerA
                    registerB = if (line[4] == 'b') registerB * 3 else registerB
                    instructionIndex++
                }

                "inc" -> {
                    registerA = if (line[4] == 'a') registerA + 1 else registerA
                    registerB = if (line[4] == 'b') registerB + 1 else registerB
                    instructionIndex++
                }

                "jmp" -> {
                    val offset = Integer.parseInt(line.substring(4))
                    instructionIndex += offset
                }

                "jie" -> {
                    val registerName = line[4]

                    if ((registerName == 'a' && registerA % 2 == 1L)
                        || (registerName == 'b' && registerB % 2 == 1L)) {
                        instructionIndex++
                        continue
                    }

                    val offset = Integer.parseInt(line.substring(7))
                    instructionIndex += offset
                }

                "jio" -> {
                    val registerName = line[4]

                    if ((registerName == 'a' && registerA != 1L)
                        || (registerName == 'b' && registerB != 1L)) {
                        instructionIndex++
                        continue
                    }

                    val offset = Integer.parseInt(line.substring(7))
                    instructionIndex += offset
                }
            }
        }

        println("register b: $registerB")
    }
}