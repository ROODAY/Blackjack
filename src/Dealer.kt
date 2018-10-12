class Dealer(private val isAI: Boolean, id: Int, money: Int) : Player(id, money) {

    fun showFirstCard() {
        println("The Dealer shows the ${hand.cards[0]}")
    }

    fun getIsAI(): Boolean {
        return isAI
    }

    fun autoRunTurn(deck: Deck, stopAt: Int) {
        while (hand.value < stopAt) {
            hand.add(deck.draw())
            println("Dealer hits (${hand.value}):")
            println(hand)
        }
    }
}