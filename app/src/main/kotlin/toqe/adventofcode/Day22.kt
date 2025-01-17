package toqe.adventofcode

class Day22 {
    val spells = arrayOf(
        Spell(SpellType.MagicMissile, 53),
        Spell(SpellType.Drain, 73),
        Spell(SpellType.Shield, 113),
        Spell(SpellType.Poison, 173),
        Spell(SpellType.Recharge, 229),
    )

    fun run() {
        val enableLog = false
        val myHitPoints = 50
        val myMana = 500
        val input = InputFileHelper.readInput(22)
        val inputLines = input.trim().split('\n').map { it.split(": ") }
        val bossHitPoints = Integer.parseInt(inputLines[0][1])
        val bossDamage = Integer.parseInt(inputLines[1][1])
        val gameStatus = GameStatus(myHitPoints, myMana, bossHitPoints, bossDamage, Mode.Part2, enableLog)
        val minMana = fightAndGetMinManaForWinMyTurn(gameStatus)
        println("minMana $minMana")
    }

    fun fightAndGetMinManaForWinMyTurn(gameStatus: GameStatus): Int {
        var minResult = Int.MAX_VALUE

        if (gameStatus.mode == Mode.Part2) {
            gameStatus.myHitPoints--
            gameStatus.log("my hit points reduced by 1 to ${gameStatus.myHitPoints}")

            if (gameStatus.myHitPoints <= 0) {
                return minResult
            }
        }

        gameStatus.log("effect: shield ${gameStatus.shieldRemainingTurns} poison ${gameStatus.poisonRemainingTurns} recharge ${gameStatus.rechargeRemainingTurns}")

        var poisonActive = gameStatus.poisonRemainingTurns > 0
        var rechargeActive = gameStatus.rechargeRemainingTurns > 0

        applyEffects(gameStatus)

        if (poisonActive) {
            val previousBossHitPoints = gameStatus.bossHitPoints
            gameStatus.bossHitPoints -= 3
            gameStatus.log("poison: boss hit points reduced $previousBossHitPoints -> ${gameStatus.bossHitPoints}")
        }

        if (rechargeActive) {
            val previousMyMana = gameStatus.myMana
            gameStatus.myMana += 101
            gameStatus.log("recharge: my mana increased $previousMyMana -> ${gameStatus.myMana}")
        }

        if (gameStatus.bossHitPoints <= 0) {
            gameStatus.log("boss lost")
            return gameStatus.spentMana
        }

        for (spell in spells) {
            if (gameStatus.myMana < spell.manaCost) {
                continue
            }

            if (spell.type == SpellType.Shield && gameStatus.shieldRemainingTurns > 0) {
                continue
            }

            if (spell.type == SpellType.Poison && gameStatus.poisonRemainingTurns > 0) {
                continue
            }

            if (spell.type == SpellType.Recharge && gameStatus.rechargeRemainingTurns > 0) {
                continue
            }

            val newGameStatus = gameStatus.copy()
            newGameStatus.myMana -= spell.manaCost
            newGameStatus.spentMana += spell.manaCost

            newGameStatus.log("using spell ${spell.type} for ${spell.manaCost} mana (${newGameStatus.spentMana} total mana used, ${newGameStatus.myMana} mana available)")

            val previousBossHitPoints = newGameStatus.bossHitPoints
            val previousMyHitPoints = newGameStatus.myHitPoints

            when (spell.type) {
                SpellType.MagicMissile -> {
                    newGameStatus.bossHitPoints -= 4
                    newGameStatus.log("boss hit points reduced $previousBossHitPoints -> ${newGameStatus.bossHitPoints}")
                }

                SpellType.Drain -> {
                    newGameStatus.bossHitPoints -= 2
                    newGameStatus.myHitPoints += 2
                    newGameStatus.log("boss hit points reduced $previousBossHitPoints -> ${newGameStatus.bossHitPoints}, my hit points increased $previousMyHitPoints -> ${newGameStatus.myHitPoints}")
                }

                SpellType.Shield -> {
                    newGameStatus.shieldRemainingTurns = 6
                }

                SpellType.Poison -> {
                    newGameStatus.poisonRemainingTurns = 6
                }

                SpellType.Recharge -> {
                    newGameStatus.rechargeRemainingTurns = 5
                }
            }

            if (newGameStatus.bossHitPoints <= 0) {
                if (newGameStatus.spentMana < minResult) {
                    minResult = newGameStatus.spentMana
                }

                continue
            }

            val itemResult = fightAndGetMinManaForWinBossTurn(newGameStatus)

            if (itemResult < minResult) {
                minResult = itemResult
            }
        }

        return minResult
    }

    fun fightAndGetMinManaForWinBossTurn(gameStatus: GameStatus): Int {
        var shieldActive = gameStatus.shieldRemainingTurns > 0
        var poisonActive = gameStatus.poisonRemainingTurns > 0
        var rechargeActive = gameStatus.rechargeRemainingTurns > 0

        gameStatus.log("effect: shield ${gameStatus.shieldRemainingTurns} poison ${gameStatus.poisonRemainingTurns} recharge ${gameStatus.rechargeRemainingTurns}")

        applyEffects(gameStatus)

        if (poisonActive) {
            val previousBossHitPoints = gameStatus.bossHitPoints
            gameStatus.bossHitPoints -= 3
            gameStatus.log("poison: boss hit points reduced $previousBossHitPoints -> ${gameStatus.bossHitPoints}")
        }

        if (rechargeActive) {
            val previousMyMana = gameStatus.myMana
            gameStatus.myMana += 101
            gameStatus.log("recharge: my mana increased $previousMyMana -> ${gameStatus.myMana}")
        }

        if (gameStatus.bossHitPoints <= 0) {
            gameStatus.log("boss lost")
            return gameStatus.spentMana
        }

        val myArmor = if (shieldActive) 7 else 0

        val previousMyHitPoints = gameStatus.myHitPoints
        gameStatus.myHitPoints -= Math.max(1, gameStatus.bossDamage - myArmor)
        gameStatus.log("boss attacks, my hit points reduced $previousMyHitPoints -> ${gameStatus.myHitPoints}")

        if (gameStatus.myHitPoints <= 0) {
            return Int.MAX_VALUE
        }

        return fightAndGetMinManaForWinMyTurn(gameStatus)
    }

    fun applyEffects(gameStatus: GameStatus) {
        if (gameStatus.shieldRemainingTurns > 0) {
            gameStatus.shieldRemainingTurns--
        }

        if (gameStatus.poisonRemainingTurns > 0) {
            gameStatus.poisonRemainingTurns--
        }

        if (gameStatus.rechargeRemainingTurns > 0) {
            gameStatus.rechargeRemainingTurns--
        }
    }

    data class GameStatus(
        var myHitPoints: Int,
        var myMana: Int,
        var bossHitPoints: Int,
        var bossDamage: Int,
        var mode: Mode,
        val enableLog: Boolean,
        var spentMana: Int = 0,
        var shieldRemainingTurns: Int = 0,
        var poisonRemainingTurns: Int = 0,
        var rechargeRemainingTurns: Int = 0,
        var log: String = ""
    ) {
        fun log(text: String) {
            if (!enableLog) {
                return
            }

            log += text + "\n"
        }
    }

    data class Spell(val type: SpellType, val manaCost: Int) {
    }

    enum class SpellType {
        MagicMissile,
        Drain,
        Shield,
        Poison,
        Recharge,
    }

    enum class Mode {
        Part1,
        Part2,
    }
}