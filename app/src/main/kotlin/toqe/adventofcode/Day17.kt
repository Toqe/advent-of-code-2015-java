package toqe.adventofcode

class Day17 {
    fun run() {
        val exampleContainers = arrayOf(20, 15, 10, 5, 5)
        val exampleLiters = 25
        val exampleNumberOfContainersCountMap = HashMap<Int, Int>()
        val exampleCombinations =
            findCombinations(exampleContainers, 0, 0, 0, exampleLiters, exampleNumberOfContainersCountMap)
        println("exampleCombinations $exampleCombinations")

        val input = InputFileHelper.readInput(17)
        val targetLiters = 150
        val containers = input.trim().split('\n').map { Integer.parseInt(it) }.toTypedArray()
        val numberOfContainersCountMap = HashMap<Int, Int>()
        val combinations = findCombinations(containers, 0, 0, 0, targetLiters, numberOfContainersCountMap)
        println("combinations $combinations")
        val minContainersCombinations = numberOfContainersCountMap[numberOfContainersCountMap.keys.min()]
        println("minContainersCombinations $minContainersCombinations")
    }

    fun findCombinations(
        containers: Array<Int>,
        containerIndex: Int,
        currentLiters: Int,
        currentNumberOfContainers: Int,
        targetLiters: Int,
        numberOfContainersCountMap: HashMap<Int, Int>
    ): Long {
        if (containerIndex == containers.size) {
            if (currentLiters == targetLiters) {
                if (!numberOfContainersCountMap.containsKey(currentNumberOfContainers)) {
                    numberOfContainersCountMap[currentNumberOfContainers] = 0
                }

                numberOfContainersCountMap[currentNumberOfContainers] =
                    numberOfContainersCountMap[currentNumberOfContainers]!! + 1

                return 1
            }

            return 0
        }

        val currentContainer = containers[containerIndex]

        var result = findCombinations(
            containers,
            containerIndex + 1,
            currentLiters,
            currentNumberOfContainers,
            targetLiters,
            numberOfContainersCountMap
        )

        if (currentLiters + currentContainer <= targetLiters) {
            result += findCombinations(
                containers,
                containerIndex + 1,
                currentLiters + currentContainer,
                currentNumberOfContainers + 1,
                targetLiters,
                numberOfContainersCountMap
            )
        }

        return result
    }
}