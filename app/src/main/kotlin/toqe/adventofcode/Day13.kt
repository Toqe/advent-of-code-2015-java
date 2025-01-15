package toqe.adventofcode

class Day13 {
    val regex = "([A-Za-z]+) would (gain|lose) (\\d+) happiness units by sitting next to ([A-Za-z]+)\\.".toRegex()

    fun run() {
        val exampleInput = """
            Alice would gain 54 happiness units by sitting next to Bob.
            Alice would lose 79 happiness units by sitting next to Carol.
            Alice would lose 2 happiness units by sitting next to David.
            Bob would gain 83 happiness units by sitting next to Alice.
            Bob would lose 7 happiness units by sitting next to Carol.
            Bob would lose 63 happiness units by sitting next to David.
            Carol would lose 62 happiness units by sitting next to Alice.
            Carol would gain 60 happiness units by sitting next to Bob.
            Carol would gain 55 happiness units by sitting next to David.
            David would gain 46 happiness units by sitting next to Alice.
            David would lose 7 happiness units by sitting next to Bob.
            David would gain 41 happiness units by sitting next to Carol.
        """.trimIndent()

        val exampleMap = parse(exampleInput)
        val exampleMaxHappiness = getMaxHappiness(Array<String>(exampleMap.keys.size) { "" }, exampleMap.keys.toHashSet(), exampleMap, 0)
        println("exampleMaxHappiness $exampleMaxHappiness")

        // part 1
        val input = InputFileHelper.readInput(13)
        val map = parse(input)
        val maxHappiness = getMaxHappiness(Array<String>(map.keys.size) { "" }, map.keys.toHashSet(), map, 0)
        println("maxHappiness $maxHappiness")

        // part 2
        val me = "Me"
        val meList = ArrayList<Pair<String, Int>>()

        for ((k,v) in map) {
            v.add(Pair(me, 0))
            meList.add(Pair(k, 0))
        }

        map.put(me, meList)
        val maxHappinessPart2 = getMaxHappiness(Array<String>(map.keys.size) { "" }, map.keys.toHashSet(), map, 0)
        println("maxHappinessPart2 $maxHappinessPart2")
    }

    fun parse(input: String): HashMap<String, ArrayList<Pair<String, Int>>> {
        val result = HashMap<String, ArrayList<Pair<String, Int>>>()

        for (line in input.split("\n")) {
            if (line == "") {
                continue
            }

            val match = regex.matchEntire(line)

            if (match == null) {
                throw Exception("could not regex match line '$line'")
            }

            val person1 = match.groups[1]?.value!!
            val happinessGain = (if (match.groups[2]?.value == "gain") 1 else -1) * Integer.parseInt(match.groups[3]?.value!!)
            val person2 = match.groups[4]?.value!!

            if (!result.containsKey(person1)) {
                result.put(person1, ArrayList<Pair<String, Int>>())
            }

            result.get(person1)!!.add(Pair(person2, happinessGain))
        }

        return result
    }

    fun getMaxHappiness(
        placement: Array<String>,
        todos: HashSet<String>,
        map: Map<String, ArrayList<Pair<String, Int>>>,
        index: Int
    ): Int {
        if (index >= placement.size) {
            var sum = 0

            for (i in 0..placement.size - 1) {
                val leftIndex = if (i == 0) placement.size - 1 else i - 1
                val rightIndex = if (i == placement.size - 1) 0 else i + 1
                val mapEntry = map[placement[i]]!!
                val leftEntry = mapEntry.first { it.first == placement[leftIndex] }
                val rightEntry = mapEntry.first { it.first == placement[rightIndex] }
                sum += leftEntry.second + rightEntry.second
            }

            return sum
        }

        var max = Int.MIN_VALUE

        for (todo in todos) {
            val newPlacement = placement.clone()
            newPlacement[index] = todo
            val current = getMaxHappiness(newPlacement, todos.filter { it != todo }.toHashSet(), map, index + 1)

            if (current > max) {
                max = current
            }
        }

        return max
    }
}