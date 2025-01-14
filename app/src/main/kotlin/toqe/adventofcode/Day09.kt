package toqe.adventofcode

class Day09 {
    val lineRegex = "([A-Za-z]+) to ([A-Za-z]+) = ([0-9]+)".toRegex()

    fun run() {
        val exampleInput = """
            London to Dublin = 464
            London to Belfast = 518
            Dublin to Belfast = 141
        """.trimIndent()

        val exampleMinMaxDistance = getMinimumMaximumDistance(exampleInput)
        println("example min/max distance: $exampleMinMaxDistance")

        val input = InputFileHelper.readInput(9)
        val minMaxDistance = getMinimumMaximumDistance(input)
        println("min/max distances: $minMaxDistance")
    }

    fun getMinimumMaximumDistance(input: String): Pair<Int, Int> {
        val distances = HashMap<Pair<String, String>, Int>()
        val todoLocations = HashSet<String>()

        for (line in input.split("\n")) {
            if (line == "") {
                continue
            }

            val match = lineRegex.matchEntire(line)

            if (match == null) {
                throw Exception("line '$line' does not match")
            }

            val from = match.groups[1]?.value!!
            val to = match.groups[2]?.value!!
            val distance = Integer.parseInt(match.groups[3]?.value)
            distances.put(Pair(from, to), distance)
            distances.put(Pair(to, from), distance)
            todoLocations.add(from)
            todoLocations.add(to)
        }

        val result = getMinimumMaximumDistance(null, 0, distances, todoLocations, HashSet<String>())
        return result
    }

    fun getMinimumMaximumDistance(
        lastLocation: String?,
        distance: Int,
        distances: HashMap<Pair<String, String>, Int>,
        todoLocations: HashSet<String>,
        doneLocations: HashSet<String>
    ): Pair<Int, Int> {
        if (todoLocations.isEmpty()) {
            return Pair(distance, distance)
        }

        var minimum = Int.MAX_VALUE
        var maximum = 0

        for (location in todoLocations) {
            val newDistance = distance + if (lastLocation != null) distances[Pair(lastLocation, location)]!! else 0

            val innerResult = getMinimumMaximumDistance(
                location,
                newDistance,
                distances,
                todoLocations.filter { it != location }.toHashSet(),
                (doneLocations + location).toHashSet()
            )

            if (innerResult.first < minimum) {
                minimum = innerResult.first
            }

            if (innerResult.second > maximum) {
                maximum = innerResult.second
            }
        }

        return Pair(minimum, maximum)
    }
}