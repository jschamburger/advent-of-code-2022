fun main() {

    val input = readInput("Day02")

    // part 1
    val sum = input.sumOf {
        if (it.isNotBlank()) {
            val split = it.split(" ")
            val opponent = split[0]
            val player = split[1]
            calculateScore(opponent, player)
        } else 0
    }
    sum.println()

    // part 2
    val sum2 = input.sumOf {
        if (it.isNotBlank()) {
            val split = it.split(" ")
            val opponent = split[0]
            val outcome = split[1]
            calculateScoreWithOutcome(opponent, outcome)
        } else 0
    }
    sum2.println()
}

fun calculateScore(opponent: String, player: String): Int {
    val opponentShape = Shape.fromString(opponent)
    return when (Shape.fromString(player)) {
        Shape.ROCK -> {
            1 + when (opponentShape) {
                Shape.ROCK -> 3
                Shape.PAPER -> 0
                Shape.SCISSORS -> 6
            }
        }

        Shape.PAPER -> {
            2 + when (opponentShape) {
                Shape.ROCK -> 6
                Shape.PAPER -> 3
                Shape.SCISSORS -> 0
            }
        }

        Shape.SCISSORS -> {
            3 + when (opponentShape) {
                Shape.ROCK -> 0
                Shape.PAPER -> 6
                Shape.SCISSORS -> 3
            }
        }
    }
}

fun calculateScoreWithOutcome(opponent: String, outcomeString: String): Int {
    val outcome = Outcome.fromString(outcomeString)
    val playerShape = when (Shape.fromString(opponent)) {
        Shape.ROCK -> {
            when (outcome) {
                Outcome.LOSE -> Shape.SCISSORS
                Outcome.DRAW -> Shape.ROCK
                Outcome.WIN -> Shape.PAPER
            }
        }
        Shape.PAPER -> {
            when (outcome) {
                Outcome.LOSE -> Shape.ROCK
                Outcome.DRAW -> Shape.PAPER
                Outcome.WIN -> Shape.SCISSORS
            }
        }
        Shape.SCISSORS -> {
            when (outcome) {
                Outcome.LOSE -> Shape.PAPER
                Outcome.DRAW -> Shape.SCISSORS
                Outcome.WIN -> Shape.ROCK
            }
        }
    }
    return calculateScore(opponent, playerShape.playerStringValue)
}

enum class Shape(private val opponentStringValue: String, val playerStringValue: String) {
    ROCK("A", "X"), PAPER("B", "Y"), SCISSORS("C", "Z");

    companion object {
        fun fromString(string: String): Shape {
            return Shape.values().first { shape -> shape.opponentStringValue == string || shape.playerStringValue == string }
        }
    }
}

enum class Outcome(private val stringValue: String) {
    LOSE("X"), DRAW("Y"), WIN("Z");

    companion object {
        fun fromString(string: String): Outcome {
            return Outcome.values().first { it.stringValue == string }
        }
    }
}
