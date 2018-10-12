import kotlin.system.exitProcess

abstract class CardGame {

    var players = mutableListOf<Player>()

    private fun initPlayers(gameName: String, startCash: Int) {
        println()
        println("Welcome to $gameName! How many players? (Between 1 and 9)")
        var choosing = true
        while (choosing) {
            val input = readLine()?.toInt() as Int
            when (input) {
                1 -> {
                    players.add(Player(1, startCash))
                    players.add(Dealer(true, 2, startCash * 3))
                    choosing = false
                }
                2,3,4,5,6,7,8,9 -> {
                    players.add(Dealer(false, 1, startCash * 3))

                    for (i in 1..(input - 1)) {
                        players.add(Player(i + 1, startCash))
                    }

                    players.shuffle()

                    println("Player ${players.indexOf(players.find { it is Dealer }) + 1} is the Dealer!")
                    choosing = false
                }
                else -> println("Invalid input! Please try again.")
            }
        }
    }

    fun runGame(gameName: String, startCash: Int, numDecks: Int) {
        initPlayers(gameName, startCash)

        while (true) {
            if (players.size <= 0) exitProcess(0)
            if (players.find { it is Dealer }?.money!! <= 0) {
                println("Dealer is out of cash! Game Over!")
                exitProcess(0)
            }
            println("\n\n\n\n\n\n\n\n\n\n\n\n")
            println("New Round:")
            runRound(Deck(numDecks))
        }
    }

    abstract fun runRound(deck: Deck)
    abstract fun dealCards(players: MutableList<Player>, deck: Deck, cardsToDeal: Int)
    abstract fun setPlayerBet(player: Player)
    abstract fun runPlayerTurn(player: Player, deck: Deck)
    abstract fun runDealerTurn(dealer: Dealer, deck: Deck)
    abstract fun compareHands(players: MutableList<Player>)
    abstract fun calculateHandValue(hand: Hand): Int
}