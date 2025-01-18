package toqe.adventofcode

class Day24 {
    fun run() {
        val input = InputFileHelper.readInput(24)
        val weights = input.trim().split('\n').map { Integer.parseInt(it) }.toIntArray()

        val supposedWeightPart1 = weights.sum() / 3
        val bestDistributionPart1 = checkDistributions(weights, 0, supposedWeightPart1)
        println("part 1: ${bestDistributionPart1.qe}")

        val supposedWeightPart2 = weights.sum() / 4
        val bestDistributionPart2 = checkDistributions(weights, 0, supposedWeightPart2)
        println("part 2: ${bestDistributionPart2.qe}")
    }

    fun checkDistributions(
        weights: IntArray,
        index: Int,
        supposedWeight: Int,
        group1Weight: Int = 0,
        group1Qe: Long = 1,
        group1Count: Int = 0,
        bestDistribution: BestDistribution = BestDistribution(Int.MAX_VALUE, Long.MAX_VALUE),
    ): BestDistribution {
        if (index == weights.size) {
            if (group1Weight == supposedWeight) {
                if (group1Count < bestDistribution.count) {
                    bestDistribution.count = group1Count
                    bestDistribution.qe = group1Qe
                }

                if (group1Count == bestDistribution.count && group1Qe < bestDistribution.qe) {
                    bestDistribution.count = group1Count
                    bestDistribution.qe = group1Qe
                }
            }

            return bestDistribution
        }

        checkDistributions(
            weights,
            index + 1,
            supposedWeight,
            group1Weight,
            group1Qe,
            group1Count,
            bestDistribution,
        )

        val weight = weights[index]

        if (group1Weight + weight <= supposedWeight) {
            checkDistributions(
                weights,
                index + 1,
                supposedWeight,
                group1Weight + weight,
                group1Qe * weight,
                group1Count + 1,
                bestDistribution,
            )
        }

        return bestDistribution
    }

    data class BestDistribution(var count: Int, var qe: Long) {
    }
}