open class Player(var id: Int, var money: Int) {
    var bet = 0
    val hands = mutableListOf(Hand(null))
    var folded = false
    var busted = false

    val hand: Hand
        get() = hands[0]

    fun reset() {
        folded = false
        busted = false
        bet = 0
        hands.clear()
        hands.add(Hand(null))
    }

    companion object {
        fun printPlayerOptions(options: MutableList<String>) {
            println("What would you like to do?")
            for ((index, option) in options.iterator().withIndex()) {
                println("${index + 1}: $option")
            }
        }
    }
}