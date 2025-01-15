package toqe.adventofcode

class Day14 {
    val regex = "([A-Za-z]+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds\\.".toRegex()

    fun run() {
        val exampleInput = """
            Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
            Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
        """.trimIndent()

        val exampleDuration = 1000
        val exampleReindeers = parseReindeers(exampleInput)
        val exampleMaxDistance = exampleReindeers.map { getDistance(it, exampleDuration) }.max()
        println("exampleMaxDistance $exampleMaxDistance")

        val duration = 2503
        val input = InputFileHelper.readInput(14)
        val reindeers = parseReindeers(input)
        val maxDistance = reindeers.map { getDistance(it, duration) }.max()
        println("maxDistance $maxDistance")

        // part 2
        val exampleMaxScore = getMaxScore(exampleReindeers, exampleDuration)
        println("exampleMaxScore $exampleMaxScore")

        var maxScore = getMaxScore(reindeers, duration)
        println("maxScore $maxScore")
    }

    fun parseReindeers(input: String): List<Reindeer> {
        val result = ArrayList<Reindeer>()

        for (line in input.split("\n")) {
            if (line == "") {
                continue
            }

            val match = regex.matchEntire(line)

            if (match == null) {
                throw Exception("could not parse line '$line'")
            }

            val name = match.groups[1]?.value!!
            val flySpeed = Integer.parseInt(match.groups[2]?.value!!)
            val flyDuration = Integer.parseInt(match.groups[3]?.value!!)
            val restDuration = Integer.parseInt(match.groups[4]?.value!!)

            result.add(Reindeer(name, flySpeed, flyDuration, restDuration))
        }

        return result
    }

    fun getDistance(reindeer: Reindeer, duration: Int): Int {
        var resting = false;
        var remainingDuration = duration
        var result = 0

        while (remainingDuration > 0) {
            if (resting) {
                val restDuration = Math.min(reindeer.restDuration, remainingDuration)
                remainingDuration -= restDuration
                resting = false
            } else {
                val flyDuration = Math.min(reindeer.flyDuration, remainingDuration)
                result += flyDuration * reindeer.flySpeed
                remainingDuration -= flyDuration
                resting = true
            }
        }

        return result
    }

    fun getMaxScore(reindeers: List<Reindeer>, duration: Int) : Int {
        val reindeerStatus = HashMap<String, ReindeerStatus>()

        for (reindeer in reindeers) {
            val status = ReindeerStatus()
            status.remainingDurationInStatus = reindeer.flyDuration
            reindeerStatus.put(reindeer.name, status)
        }

        for (timestamp in 1..duration) {
            var maxDistanceTravelled = 0

            for (reindeer in reindeers) {
                val status = reindeerStatus[reindeer.name]!!

                if (!status.isResting) {
                    status.distanceTravelled += reindeer.flySpeed
                }

                status.remainingDurationInStatus--

                if (status.remainingDurationInStatus == 0) {
                    status.isResting = !status.isResting
                    status.remainingDurationInStatus = if (status.isResting) reindeer.restDuration else reindeer.flyDuration
                }

                if (status.distanceTravelled > maxDistanceTravelled) {
                    maxDistanceTravelled = status.distanceTravelled
                }
            }

            for (reindeer in reindeers) {
                val status = reindeerStatus[reindeer.name]!!

                if (status.distanceTravelled == maxDistanceTravelled) {
                    status.score++
                }
            }
        }

        return reindeerStatus.maxOf { it.value.score }
    }

    data class Reindeer(val name: String, val flySpeed: Int, val flyDuration: Int, val restDuration: Int) {
    }

    class ReindeerStatus {
        var isResting = false
        var distanceTravelled = 0
        var remainingDurationInStatus = 0
        var score = 0
    }
}