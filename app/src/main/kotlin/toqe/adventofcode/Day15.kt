package toqe.adventofcode

import kotlin.Int

class Day15 {
    val regex =
        "([A-Za-z]+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)".toRegex()

    fun run() {
        val teaspoons = 100

        val exampleInput = """
            Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
            Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
        """.trimIndent()

        val exampleData = parse(exampleInput)
        val exampleMaxScore = distributeAndGetMaxScore(IntArray(exampleData.size) { 0 }, exampleData, 0, teaspoons, Mode.Part1)
        println("exampleMaxScore $exampleMaxScore")

        val input = InputFileHelper.readInput(15)
        val data = parse(input)
        val maxScore = distributeAndGetMaxScore(IntArray(data.size) { 0 }, data, 0, teaspoons, Mode.Part1)
        println("maxScore $maxScore")

        // part 2
        val exampleMaxScorePart2 = distributeAndGetMaxScore(IntArray(exampleData.size) { 0 }, exampleData, 0, teaspoons, Mode.Part2)
        println("exampleMaxScorePart2 $exampleMaxScorePart2")

        val maxScorePart2 = distributeAndGetMaxScore(IntArray(data.size) { 0 }, data, 0, teaspoons, Mode.Part2)
        println("maxScorePart2 $maxScorePart2")
    }

    fun parse(input: String): List<Ingredient> {
        val result = ArrayList<Ingredient>()

        for (line in input.split("\n")) {
            if (line == "") {
                continue
            }

            val match = regex.matchEntire(line)

            if (match == null) {
                throw Exception("Could not parse line '$line'")
            }

            val ingredient = Ingredient(
                match.groups[1]?.value!!,
                Integer.parseInt(match.groups[2]?.value!!),
                Integer.parseInt(match.groups[3]?.value!!),
                Integer.parseInt(match.groups[4]?.value!!),
                Integer.parseInt(match.groups[5]?.value!!),
                Integer.parseInt(match.groups[6]?.value!!),
            )

            result.add(ingredient)
        }

        return result
    }

    fun distributeAndGetMaxScore(
        distribution: IntArray,
        ingredients: List<Ingredient>,
        ingredientIndex: Int,
        remainingTeaspoons: Int,
        mode: Mode
    ): Long {
        if (ingredientIndex == ingredients.size - 1) {
            distribution[ingredientIndex] = remainingTeaspoons

            var capacity = 0L
            var durability = 0L
            var flavor = 0L
            var texture = 0L
            var calories = 0L

            for (i in 0..distribution.size - 1) {
                capacity += distribution[i] * ingredients[i].capacity
                durability += distribution[i] * ingredients[i].durability
                flavor += distribution[i] * ingredients[i].flavor
                texture += distribution[i] * ingredients[i].texture
                calories += distribution[i] * ingredients[i].calories
            }

            if (capacity < 0 || durability < 0 || flavor < 0 || texture < 0) {
                return 0L
            }

            if (mode == Mode.Part2 && calories != 500L) {
                return 0L
            }

            return capacity * durability * flavor * texture
        }

        var maxScore = 0L
        val reservedForFollowingIngredients = ingredients.size - ingredientIndex - 1
        var maxTeaspoonsForThisIngredient = remainingTeaspoons - reservedForFollowingIngredients

        for (i in 1..maxTeaspoonsForThisIngredient) {
            distribution[ingredientIndex] = i
            val score = distributeAndGetMaxScore(distribution, ingredients, ingredientIndex + 1, remainingTeaspoons - i, mode)

            if (score > maxScore) {
                maxScore = score
            }
        }

        return maxScore
    }

    data class Ingredient(
        val name: String,
        val capacity: Int,
        val durability: Int,
        val flavor: Int,
        val texture: Int,
        val calories: Int
    ) {
    }

    enum class Mode {
        Part1,
        Part2
    }
}