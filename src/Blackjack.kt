import kotlin.system.exitProcess

class Blackjack : CardGame() {
    override fun runRound(deck: Deck) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun runPlayerTurn(player: Player, deck: Deck) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPlayerBet(player: Player) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun runDealerTurn(dealer: Dealer, deck: Deck) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun compareHands(players: MutableList<Player>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun calculateHandValue(hand: Hand): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dealCards(players: MutableList<Player>, deck: Deck, cardsToDeal: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*
    //val players = mutableListOf(Player())
    var playerBusted = false
    var dealerBusted = false
    var bet = 0

    override fun run() {
        initPlayers()
        while (true) {
            val player = players.find { it !is Dealer } as Player
            if (player.money <= 0) {
                println("You've ran out of money! Game Over!")
                println("Thanks for playing!")
                exitProcess(0)
            } else {
                println("New round!")
                runRound(Deck())
            }
        }
    }

    fun initPlayers() {
        println("Welcome to Blackjack! How many players? (1 or 2)")
        var choosing = true
        while (choosing) {
            when (readLine()) {
                "1" -> {
                    players.add(Dealer(true))
                    choosing = false
                }
                "2" -> {
                    players.add(Dealer(false))
                    players.shuffle()
                    if (players[0] is Dealer) println("Player 1 is the Dealer!")
                    else println("Player 2 is the Dealer!")
                    choosing = false
                }
                else -> println("Invalid input! Please try again.")
            }
        }
    }

    fun runRound(deck: Deck) {
        val player = players.find { it !is Dealer } as Player
        val dealer = players.find { it is Dealer } as Dealer

        player.resetHands()
        dealer.resetHands()

        println("Player, what is your bet? (You currently have $${player.money})")
        var betting = true
        while (betting) {
            try {
                val input = readLine()?.toInt() as Int
                if (input <= player.money) {
                    bet = input
                    println("Bet set at $$bet!")
                    betting = false
                } else println("You don't have enough money for that bet!")
            } catch (e: NumberFormatException){
                println("Invalid input! Try again.")
            }
        }

        for (i in 1..2) {
            player.hand.add(deck.draw())
            dealer.hand.add(deck.draw())
        }

        dealer.showFirstCard()

        runPlayerTurn(player, deck)

        if (!playerBusted) runDealerTurn(player, dealer, deck)

        if (!playerBusted && !dealerBusted) compareHands(player, dealer)
    }

    fun runPlayerTurn(player: Player, deck: Deck) {
        var handsInterator = player.hands.listIterator()
        while (handsInterator.hasNext()) {
            val hand = handsInterator.next()
            val handIndex = player.hands.indexOf(hand)
            println("Hand ${handIndex + 1} (${hand.value}):")
            println(hand)

            var playingHand = true
            while (playingHand) {
                Player.printPlayerOptions()
                when (readLine()) {
                    "1" -> {
                        hand.add(deck.draw())
                        println("Hand is now (${hand.value}):")
                        println(hand)

                        if (hand.value == 21) {
                            println("Hand value is 21, stopping turn.")
                            playingHand = false
                        } else if (hand.value > 21) {
                            println("Hand value is ${hand.value}, bust!")
                            player.money -= bet
                            if (player.money <= 0) {
                                println("You've ran out of money! Game Over!")
                                println("Thanks for playing!")
                                exitProcess(0)
                            }
                            println("Ending hand.")
                            playingHand = false
                        }
                    }
                    "2" -> {
                        println("Your hand value is ${hand.value}.")
                        playingHand = false
                    }
                    "3" -> {
                        if (hand.canSplit()) {
                            val hands = hand.split()
                            hands.first.add(deck.draw())
                            hands.second.add(deck.draw())

                            handsInterator.remove()
                            handsInterator.add(hands.first)
                            handsInterator.add(hands.second)
                            handsInterator = player.hands.listIterator()

                            playingHand = false
                        } else {
                            println("You can't split any of your hands!")
                        }
                    }
                    "4" -> {
                        if (player.money >= bet * 2) {
                            bet *= 2
                            println("Bet is now $$bet!")
                            hand.add(deck.draw())
                            println("Hand is now (${hand.value}):")
                            println(hand)

                            if (hand.value > 21) {
                                println("Hand value is ${hand.value}, bust!")
                                println("Player loses $$bet!")
                                player.money -= bet
                                if (player.money <= 0) {
                                    println("You've ran out of money! Game Over!")
                                    exitProcess(0)
                                }
                            }

                            println("Ending hand.")
                            playingHand = false
                        } else {
                            println("You don't have enough money to double up!")
                        }
                    }
                    "5", "q", "Q" -> {
                        println("Thanks for playing!")
                        exitProcess(0)
                    }
                    else -> println("Invalid input! Please try again.")
                }
            }
        }

        playerBusted = true
        for (hand in player.hands) {
            if (hand.value <= 21) {
                playerBusted = false
                break
            }
        }
    }

    fun runDealerTurn(player: Player, dealer: Dealer, deck: Deck) {
        dealerBusted = false
        println("Dealer's turn. Dealer's hand (${dealer.hand.value}):")
        println(dealer.hand)

        if (dealer.getIsAI()) {
            dealer.autoRunTurn(deck)
            if (dealer.hand.value > 21) {
                println("Dealer has busted! Player wins round!")
                dealerBusted = true
                player.money += bet * 2
                return
            }
        } else if (dealer.hand.value < 17) {
            var dealerPlaying = true
            while (dealerPlaying) {
                Dealer.printDealerOptions()
                when (readLine()) {
                    "1" -> {
                        dealer.hand.add(deck.draw())
                        println("Hand is now (${dealer.hand.value}):")
                        println(dealer.hand)
                        if (dealer.hand.value > 21) {
                            println("Dealer has busted! Player wins the round!")
                            dealerBusted = true
                            player.money += bet
                            return
                        } else if (dealer.hand.value >= 17) {
                            println("Dealer must stop.")
                            dealerPlaying = false
                        }
                    }
                    "2" -> {
                        println("Standing at ${dealer.hand.value}")
                        dealerPlaying = false
                    }
                    "3", "q", "Q" -> {
                        println("Thanks for playing!")
                        exitProcess(0)
                    }
                }
            }
        }
        println("Dealer's turn over.")
    }

    fun compareHands(player: Player, dealer: Dealer) {
        println("Comparing hands:")

        for ((index, hand) in player.hands.iterator().withIndex()) {
            if (hand.value > 21) {
                println("Hand ${index + 1} busted, skipping.")
                continue
            }
            when {
                hand.value > dealer.hand.value -> {
                    println("Hand ${index + 1} beats the dealer! Player wins $${bet * 2}!")
                    player.money += bet * 2
                }
                hand.value < dealer.hand.value -> {
                    println("Hand ${index + 1} loses to the dealer! Player loses $$bet!")
                    player.money -= bet
                }
                hand.value == 21 && hand.value == dealer.hand.value -> {
                    when {
                        hand.isNaturalBlackjack() -> {
                            println("Hand ${index + 1} beats the dealer! Player wins $${bet * 2}!")
                            player.money += bet * 2
                        }
                        dealer.hand.isNaturalBlackjack() -> {
                            println("Hand ${index + 1} loses to the dealer! Player loses $$bet!")
                            player.money -= bet
                        }
                        else -> println("Hand ${index + 1} results in a draw!")
                    }
                }
                else -> println("Hand ${index + 1} results in a draw!")
            }
        }
    }*/
}