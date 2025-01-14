package toqe.adventofcode

class Day07 {
    val gates = HashMap<String, Gate>()
    val cache = HashMap<String, Int>()

    public fun run() {
        val input = InputFileHelper.readInput(7)

        for (line in input.split("\n")) {
            if (line == "") {
                continue
            }

            val gate = buildGate(line)
            gates.put(gate.wire, gate)
        }

        val wireAValue = getWireValue("a")
        println("value of wire a is $wireAValue")

        // part 2
        println("overriding b with $wireAValue and cleaning cache")
        gates.remove("b")
        gates.put("b", Gate("b", wireAValue, null, null, null, null, null))
        cache.clear()

        val wireAValuePart2 = getWireValue("a")
        println("value of wire a for part 2 is $wireAValuePart2")
    }

    fun getWireValue(wire: String): Int {
        if (cache.containsKey(wire)) {
            return cache.get(wire)!!
        }

        val gate = gates.get(wire)
        val wireValue: Int

        if (gate == null) {
            throw Exception("Gate for wire $wire not found")
        }

        if (gate.value != null) {
            wireValue = gate.value
        } else if (gate.operator == Operator.NOT) {
            wireValue = getWireValue(gate.op1!!).inv()
        } else if (gate.operator == Operator.EQ) {
            wireValue = getWireValue(gate.op1!!)
        } else {
            var x1 = if (gate.opValue1 == null) getWireValue(gate.op1!!) else gate.opValue1

            wireValue =
                    when (gate.operator) {
                        Operator.OR ->
                                x1.or(
                                        if (gate.opValue2 == null) getWireValue(gate.op2!!)
                                        else gate.opValue2
                                )
                        Operator.AND ->
                                x1.and(
                                        if (gate.opValue2 == null) getWireValue(gate.op2!!)
                                        else gate.opValue2
                                )
                        Operator.LSHIFT -> x1.shl(gate.opValue2!!)
                        Operator.RSHIFT -> x1.shr(gate.opValue2!!)
                        else -> throw Exception("unexpected operator ${gate.operator}")
                    }
        }

        cache.put(wire, wireValue)
        return wireValue
    }

    fun buildGate(line: String): Gate {
        val parts = line.split(" -> ").toTypedArray()
        val wire = parts[1]

        if ("[0-9]+".toRegex().matchEntire(parts[0]) != null) {
            return Gate(wire, Integer.parseInt(parts[0]), null, null, null, null, null)
        }

        if ("[a-z]+".toRegex().matchEntire(parts[0]) != null) {
            return Gate(wire, null, Operator.EQ, parts[0], null, null, null)
        }

        if (parts[0].startsWith("NOT")) {
            val op1 = parts[0].substring(4)
            return Gate(wire, null, Operator.NOT, op1, null, null, null)
        }

        val leftParts = parts[0].split(" ")
        var opValue1: Int? = null
        var opValue2: Int? = null

        if ("[0-9]+".toRegex().matchEntire(leftParts[0]) != null) {
            opValue1 = Integer.parseInt(leftParts[0])
        }

        if ("[0-9]+".toRegex().matchEntire(leftParts[2]) != null) {
            opValue2 = Integer.parseInt(leftParts[2])
        }

        val operator =
                when (leftParts[1]) {
                    "AND" -> Operator.AND
                    "OR" -> Operator.OR
                    "LSHIFT" -> Operator.LSHIFT
                    "RSHIFT" -> Operator.RSHIFT
                    else -> throw Exception("unknown operator '${leftParts[1]}'")
                }

        return Gate(wire, null, operator, leftParts[0], opValue1, leftParts[2], opValue2)
    }

    enum class Operator {
        OR,
        AND,
        LSHIFT,
        RSHIFT,
        NOT,
        EQ,
    }

    data class Gate(
            val wire: String,
            val value: Int?,
            val operator: Operator?,
            val op1: String?,
            val opValue1: Int?,
            val op2: String?,
            val opValue2: Int?
    ) {
        override fun toString(): String =
                "$wire value $value op $operator op1 $op1 opValue1 $opValue1 op2 $op2 opValue2 $opValue2"
    }
}
