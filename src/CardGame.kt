abstract class CardGame {
    fun initPlayers() {

    }

    fun runGame() {

    }

    abstract fun runRound()
    abstract fun runPlayerTurn()
    abstract fun runDealerTurn()
    abstract fun compareHands()

    companion object {
        const val DeckSize = 52
    }
}