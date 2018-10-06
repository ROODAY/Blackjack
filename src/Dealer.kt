class Dealer(private val isAI: Boolean) : Player() {

    fun showFirstCard() {
        println("The Dealer shows the ${hand.cards[0]}")
    }

    fun getIsAI(): Boolean {
        return isAI
    }

    fun autoRunTurn(deck: Deck) {
        while (hand.value < 17) {
            hand.add(deck.draw())
            println("Dealer hits (${hand.value}):")
            println(hand)
        }
    }

    companion object {
        fun printDealerOptions() {
            println("What would you like to do?")
            println("1: Hit")
            println("2: Stand")
            println("3: Quit")
        }
    }
}