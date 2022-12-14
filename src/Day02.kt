fun main() {

    val input = readInput("Day02")

    // part 1
    val sum = input.sumOf {
        if (it.isNotBlank()) {
            val split = it.split(" ")
            val opponent = split[0]
            val player = split[1]
            val opponentShape = SealedShape.fromString(opponent)
            val playerShape = SealedShape.fromString(player)
            calculateScore(opponentShape, playerShape)
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

fun calculateScore(opponent: SealedShape, player: SealedShape): Int {
    return player.calculateScore(opponent) + player.value
}

fun calculateScoreWithOutcome(opponent: String, outcomeString: String): Int {
    val outcome = Outcome.fromString(outcomeString)
    val opponentShape = SealedShape.fromString(opponent)
    return calculateScore(opponentShape, opponentShape.getShapeForOutcome(outcome))
}
sealed class SealedShape(val value: Int) {

    abstract val mapOutcome: Map<Outcome, SealedShape>

    fun getShapeForOutcome(outcome: Outcome) = mapOutcome[outcome.reverse()] ?: throw IllegalArgumentException()

    fun calculateScore(opponentShape: SealedShape): Int {
        val reverse = mapOutcome.reverse()
        val outcome = reverse[opponentShape] ?: throw IllegalArgumentException()
        return when (outcome) {
            Outcome.LOSE -> 0
            Outcome.DRAW -> 3
            Outcome.WIN -> 6
        }
    }
    companion object {
        fun fromString(string: String): SealedShape {
            return when (string) {
                "A", "X" -> Rock
                "B", "Y" -> Paper
                "C", "Z" -> Scissors
                else -> throw IllegalArgumentException("unknown shape")
            }
        }
    }
    object Rock : SealedShape(1) {
        override val mapOutcome = mapOf(Outcome.WIN to Scissors, Outcome.LOSE to Paper, Outcome.DRAW to Rock)
    }

    object Paper : SealedShape(2) {
        override val mapOutcome = mapOf(Outcome.WIN to Rock, Outcome.LOSE to Scissors, Outcome.DRAW to Paper)
    }

    object Scissors : SealedShape(3) {
        override val mapOutcome = mapOf(Outcome.WIN to Paper, Outcome.LOSE to Rock, Outcome.DRAW to Scissors)
    }
}

enum class Outcome(private val stringValue: String) {
    LOSE("X"), DRAW("Y"), WIN("Z");

    fun reverse(): Outcome {
        return when(this) {
            LOSE -> WIN
            DRAW -> DRAW
            WIN -> LOSE
        }
    }

    companion object {
        fun fromString(string: String): Outcome {
            return Outcome.values().first { it.stringValue == string }
        }
    }
}

fun <K, V>Map<K, V>.reverse(): Map<V, K> {
    return this.entries.associate { (k, v) -> v to k }
}
