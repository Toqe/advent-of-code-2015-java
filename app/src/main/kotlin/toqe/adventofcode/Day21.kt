package toqe.adventofcode

class Day21 {
    fun run() {
        val weapons = parseItems(
            """
            Dagger        8     4       0
            Shortsword   10     5       0
            Warhammer    25     6       0
            Longsword    40     7       0
            Greataxe     74     8       0
        """.trimIndent()
        )

        val armorItems = parseItems(
            """
            Leather      13     0       1
            Chainmail    31     0       2
            Splintmail   53     0       3
            Bandedmail   75     0       4
            Platemail   102     0       5
        """.trimIndent()
        )

        val rings = parseItems(
            """
            Damage+1    25     1       0
            Damage+2    50     2       0
            Damage+3   100     3       0
            Defense+1   20     0       1
            Defense+2   40     0       2
            Defense+3   80     0       3
        """.trimIndent()
        )

        val myHitPoints = 100
        val input = InputFileHelper.readInput(21)
        val inputSplitted = input.trim().split('\n')
        val bossHitPoints = Integer.parseInt(inputSplitted[0].split(": ")[1])
        val bossDamage = Integer.parseInt(inputSplitted[1].split(": ")[1])
        val bossArmor = Integer.parseInt(inputSplitted[2].split(": ")[1])
        var minCost = Int.MAX_VALUE
        var maxCost = Int.MIN_VALUE

        // exactly one weapon
        // armor optional
        // 0-2 rings
        for (weaponIndex in 0..weapons.size - 1) {
            val weapon = weapons[weaponIndex]

            for (armorIndex in -1..armorItems.size - 1) {
                val armor = if (armorIndex < 0) null else armorItems[armorIndex]

                for (ring1Index in -1..rings.size - 1) {
                    val ring1 = if (ring1Index < 0) null else rings[ring1Index]

                    for (ring2Index in -1..rings.size - 1) {
                        if (ring1Index >= 0 && ring2Index == ring1Index) {
                            continue
                        }

                        val ring2 = if (ring2Index < 0) null else rings[ring2Index]
                        val costDamageArmor = getCostDamageArmor(arrayOf(weapon, armor, ring1, ring2))

                        val win = doIWinTheBossFights(
                            myHitPoints,
                            costDamageArmor.second,
                            costDamageArmor.third,
                            bossHitPoints,
                            bossDamage,
                            bossArmor
                        )

                        // println("${weapon.name} ${armor?.name} ${ring1?.name} ${ring2?.name} -> ${costDamageArmor.first} $win")

                        if (win && costDamageArmor.first < minCost) {
                            minCost = costDamageArmor.first
                        }

                        if (!win && costDamageArmor.first > maxCost) {
                            maxCost = costDamageArmor.first
                        }
                    }
                }
            }
        }

        println("minCost: $minCost maxCost: $maxCost")
    }

    fun getCostDamageArmor(items: Array<Item?>): Triple<Int, Int, Int> {
        var cost = 0
        var damage = 0
        var armor = 0

        for (item in items) {
            if (item != null) {
                cost += item.cost
                damage += item.damage
                armor += item.armor
            }
        }

        return Triple(cost, damage, armor)
    }

    fun doIWinTheBossFights(
        myHitPoints: Int,
        myDamage: Int,
        myArmor: Int,
        bossHitPoints: Int,
        bossDamage: Int,
        bossArmor: Int
    ): Boolean {
        var myCurrentHitPoints = myHitPoints
        var bossCurrentHitPoints = bossHitPoints

        while (true) {
            bossCurrentHitPoints -= Math.max(1, myDamage - bossArmor)

            if (bossCurrentHitPoints <= 0) {
                return true
            }

            myCurrentHitPoints -= Math.max(1, bossDamage - myArmor)

            if (myCurrentHitPoints <= 0) {
                return false
            }
        }
    }

    fun parseItems(input: String): ArrayList<Item> {
        val result = ArrayList<Item>()

        for (line in input.trim().split('\n')) {
            val x = line.split(' ').filter { it != "" }.toTypedArray()
            result.add(Item(x[0], Integer.parseInt(x[1]), Integer.parseInt(x[2]), Integer.parseInt(x[3])))
        }

        return result
    }

    data class Item(val name: String, val cost: Int, val damage: Int, val armor: Int) {
    }
}