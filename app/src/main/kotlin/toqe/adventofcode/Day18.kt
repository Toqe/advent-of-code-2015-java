package toqe.adventofcode

class Day18 {
    fun run() {
        val exampleInput = """
            .#.#.#
            ...##.
            #....#
            ..#...
            #.#..#
            ####..
        """.trimIndent()

        val mode = Mode.Part2

        val exampleGrid = parse(exampleInput, mode)
        // print(exampleGrid)
        val exampleLights = evolveAndGetLightsOn(exampleGrid, 5, mode)
        println("exampleLights $exampleLights")

        val input = InputFileHelper.readInput(18)
        val grid = parse(input, mode)
        val lights = evolveAndGetLightsOn(grid, 100, mode)
        println("lights $lights")
    }

    fun parse(input: String, mode: Mode): Array<Array<Boolean>> {
        val lines = input.trim().split('\n')
        val grid = Array<Array<Boolean>>(lines[0].length) { Array<Boolean>(lines.size) { false } }

        for (y in 0..grid.size - 1) {
            for (x in 0..grid[0].size - 1) {
                grid[x][y] = lines[y][x] == '#'
            }
        }

        if (mode == Mode.Part2) {
            grid[0][0] = true
            grid[0][grid.size - 1] = true
            grid[grid.size - 1][0] = true
            grid[grid.size - 1][grid.size - 1] = true
        }

        return grid
    }

    fun print(grid: Array<Array<Boolean>>) {
        for (y in 0..grid.size - 1) {
            for (x in 0..grid[0].size - 1) {
                print(if (grid[x][y]) '#' else '.')
            }

            println()
        }

        println()
    }

    fun evolve(source: Array<Array<Boolean>>, target: Array<Array<Boolean>>, mode: Mode) {
        for (y in 0..source.size - 1) {
            for (x in 0..source[0].size - 1) {
                if (mode == Mode.Part2 && (x == 0 || x == source.size - 1) && (y == 0 || y == source.size - 1)) {
                    target[x][y] = true
                    continue
                }

                var sum = 0
                sum += if (x > 0 && y > 0 && source[x - 1][y - 1]) 1 else 0
                sum += if (x > 0 && source[x - 1][y]) 1 else 0
                sum += if (x > 0 && y < source.size - 1 && source[x - 1][y + 1]) 1 else 0
                sum += if (y > 0 && source[x][y - 1]) 1 else 0
                sum += if (y < source.size - 1 && source[x][y + 1]) 1 else 0
                sum += if (x < source[0].size - 1 && y > 0 && source[x + 1][y - 1]) 1 else 0
                sum += if (x < source[0].size - 1 && source[x + 1][y]) 1 else 0
                sum += if (x < source[0].size - 1 && y < source.size - 1 && source[x + 1][y + 1]) 1 else 0

                if (source[x][y]) {
                    target[x][y] = sum == 2 || sum == 3
                } else {
                    target[x][y] = sum == 3
                }
            }
        }
    }

    fun evolveAndGetLightsOn(source: Array<Array<Boolean>>, rounds: Int, mode: Mode): Int {
        var grid1 = Array<Array<Boolean>>(source.size) { Array<Boolean>(source[0].size) { false } }
        var grid2 = Array<Array<Boolean>>(source.size) { Array<Boolean>(source[0].size) { false } }

        for (round in 1..rounds) {
            if (round == 1) {
                evolve(source, grid1, mode)
            } else {
                evolve(grid1, grid2, mode)

                val swap = grid1
                grid1 = grid2
                grid2 = swap
            }

            // print(grid1)
        }

        var result = 0

        for (y in 0..grid1.size - 1) {
            for (x in 0..grid1[0].size - 1) {
                if (grid1[x][y]) {
                    result++
                }
            }
        }

        return result
    }

    enum class Mode {
        Part1,
        Part2,
    }
}