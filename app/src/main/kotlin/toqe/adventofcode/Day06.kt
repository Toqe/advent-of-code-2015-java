package toqe.adventofcode;

class Day06 {
    val regex = "(turn off|turn on|toggle) ([0-9]+),([0-9]+) through ([0-9]+),([0-9]+)".toRegex()

    public fun run() {
        val lights = Array(1000) { Array<Boolean>(1000) { false } }
        val input = InputFileHelper.readInput(6)

        for (line in input.split("\n")) {
            if (line == "") {
                continue
            }

            val instruction = readInstruction(line)
            apply(instruction, lights)
        }

        var count = 0

        for (y in 0..999) {
            for (x in 0..999) {
                if (lights[y][x]) {
                    count++
                }
            }
        }

        println("count: " + count)
    }

    public fun apply(instruction : Triple<ChangeLightType, Pair<Int, Int>, Pair<Int, Int>>, lights : Array<Array<Boolean>>) {
        for (y in instruction.second.second..instruction.third.second) {
            for (x in instruction.second.first..instruction.third.first) {
                when (instruction.first) {
                    ChangeLightType.TURN_ON -> lights[y][x] = true
                    ChangeLightType.TURN_OFF -> lights[y][x] = false
                    ChangeLightType.TOGGLE -> lights[y][x] = !lights[y][x]
                }
            }
        }
    }

    public fun readInstruction(instruction: String) : Triple<ChangeLightType, Pair<Int, Int>, Pair<Int, Int>> {
        val match = regex.matchEntire(instruction)

        val type = if (match!!.groups[1]!!.value == "turn on") 
            ChangeLightType.TURN_ON 
            else (if (match.groups[1]!!.value == "turn off") 
                ChangeLightType.TURN_OFF 
                else ChangeLightType.TOGGLE)
        
        val fromX = Integer.parseInt(match.groups[2]!!.value)
        val fromY = Integer.parseInt(match.groups[3]!!.value)
        val toX = Integer.parseInt(match.groups[4]!!.value)
        val toY = Integer.parseInt(match.groups[5]!!.value)

        return Triple(type, Pair(fromX, fromY), Pair(toX, toY))
    }

    enum class ChangeLightType {
        TURN_ON,
        TURN_OFF,
        TOGGLE,
    }
}