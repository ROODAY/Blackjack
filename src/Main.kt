fun main(args: Array<String>) {
    println("Welcome to the 591 D1 Card Game Suite!")
    println("Which game would you like to play?")
    println("(1) Blackjack")
    println("(2) Trianta Ena")

    var choosing = true
    while (choosing) {
        when (readLine()) {
            "1" -> {
                choosing = false
                val game = Blackjack()
                game.runGame("Blackjack", 100, 1)
            }
            "2" -> {
                choosing = false
                val game = TriantaEna()
                game.runGame("Trianta Ena", 100, 2)
            }
            else -> println("Invalid input! Please try again.")
        }
    }
}