package toqe.adventofcode

class Day19 {
    fun run() {
        val replacements = HashMap<String, ArrayList<String>>()
        val generatedMolecules = HashSet<String>()
        val input = InputFileHelper.readInput(19)
        val inputSplitted = input.trim().split("\n\n")
        val inputMolecule = inputSplitted[1]

        for (line in inputSplitted[0].split('\n')) {
            val lineSplitted = line.split(" => ")
            val key = lineSplitted[0]
            val value = lineSplitted[1]

            if (!replacements.containsKey(key)) {
                replacements.put(key, ArrayList<String>())
            }

            replacements[key]!!.add(value)
        }

        for (i in 0..inputMolecule.length - 1) {
            val sub1 = inputMolecule.substring(i, i + 1)

            if (replacements.containsKey(sub1)) {
                for (replacement in replacements[sub1]!!) {
                    generatedMolecules.add(inputMolecule.substring(0, i) + replacement + inputMolecule.substring(i + 1))
                }
            }

            if (i < inputMolecule.length - 1) {
                val sub2 = inputMolecule.substring(i, i + 2)

                if (replacements.containsKey(sub2)) {
                    for (replacement in replacements[sub2]!!) {
                        generatedMolecules.add(inputMolecule.substring(0, i) + replacement + inputMolecule.substring(i + 2))
                    }
                }
            }
        }

        println(generatedMolecules.size)

        // part 2 - really bad as a general solution, but works...
        var count = 0
        var m = inputMolecule
        val replacementStrings = inputSplitted[0].split('\n').map{ it.split(" => ") }

        while (m != "e") {
            for (r in replacementStrings) {
                if (m.contains(r[1])) {
                    val index = m.indexOf(r[1])
                    m = m.substring(0, index) + r[0] + m.substring(index + r[1].length)
                    count++
                }
            }
        }

        println(count)
    }
}