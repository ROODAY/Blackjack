open class Player {
    var money = 100
    val hands = mutableListOf(Hand(null))

    val hand: Hand
        get() = hands[0]

    fun resetHands() {
        hands.clear()
        hands.add(Hand(null))
    }

    companion object {
        fun printPlayerOptions() {
            println("What would you like to do?")
            println("1: Hit")
            println("2: Stand")
            println("3: Split")
            println("4: Double Up")
            println("5: Quit")
        }
    }
}