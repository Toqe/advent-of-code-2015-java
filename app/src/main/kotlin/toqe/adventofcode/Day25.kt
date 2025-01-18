package toqe.adventofcode

class Day25 {
    fun run() {
        val input = InputFileHelper.readInput(25)
        val match = "row (\\d+), column (\\d+)".toRegex().find(input)

        if (match == null) {
            throw Exception("")
        }

        val row = Integer.parseInt(match.groups[1]?.value!!)
        val col = Integer.parseInt(match.groups[2]?.value!!)

        var index = 1
        var currentRow = 1
        var currentCol = 1
        var currentCode = 20151125L

        while(true) {
            if (row == currentRow && col == currentCol) {
                break
            }

            if (currentRow == 1) {
                currentRow = currentCol + 1
                currentCol = 1
            } else {
                currentRow--
                currentCol++
            }

            currentCode = currentCode * 252533L % 33554393L
            index++
        }

        println("code $currentCode")
    }
}