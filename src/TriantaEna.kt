import kotlin.system.exitProcess

class TriantaEna: CardGame() {

    override fun calculateHandValue(hand: Hand): Int {
        return 1
    }

    override fun dealCards(players: MutableList<Player>, deck: Deck, cardsToDeal: Int) {
        for (player in players) {
            for (i in 1..cardsToDeal) player.hand.add(deck.draw())
            if (player is Dealer) {
                println("Dealer's Hand (${player.hand.value}):")
            } else {
                println("Player ${player.id}'s Hand (${player.hand.value}):")
            }
            println(player.hand)
            println("Press enter to continue...")
            readLine()
            println("\n\n\n\n\n\n\n\n\n\n\n\n")
        }
    }

    override fun runRound(deck: Deck) {
        // Reset players for new round
        for (player in players) {
            player.reset()
        }

        println("Dealing Cards:")
        dealCards(players, deck, 1)

        // Show Dealer card to players
        (players.find { it is Dealer } as Dealer).showFirstCard()

        println("Collecting Player Bets:")
        for (player in players) {
            if (player !is Dealer) {
                setPlayerBet(player)
            }
        }
        println("\n\n\n\n\n\n\n\n\n\n\n\n")

        println("Dealing more cards:")
        for (player in players) {
            if (player !is Dealer && !player.folded) {
                player.hand.add(deck.draw())
                player.hand.add(deck.draw())
                println("Player ${player.id}'s Hand (${player.hand.value}):")
                println(player.hand)
                println("Press enter to continue...")
                readLine()
                println("\n\n\n\n\n\n\n\n\n\n\n\n")
            }
        }

        println("Running player turns:")
        for (player in players) {
            if (player !is Dealer) {
                runPlayerTurn(player, deck)
            }
        }
        println("\n\n\n\n\n\n\n\n\n\n\n\n")

        println("Dealer's hand (${players.find { it is Dealer }?.hand?.value}):")
        println(players.find { it is Dealer }?.hand)
        runDealerTurn(players.find { it is Dealer } as Dealer, deck)
        println("\n\n\n\n\n\n\n\n\n\n\n\n")

        compareHands(players)
        println("\n\n\n\n\n\n\n\n\n\n\n\n")

        rotateDealer(players)
    }

    override fun setPlayerBet(player: Player) {
        println("Player ${player.id}, what is your bet? (You currently have $${player.money}) Your Hand (${player.hand.value}):")
        println(player.hand)
        println("You may also type \"fold\" to skip this round.")
        var betting = true
        while (betting) {
            try {
                val input = readLine()
                if (input == "fold") {
                    println("Folded! Skipping you for this round.")
                    player.folded = true
                    betting = false
                } else {
                    val betValue = input?.toInt() as Int
                    if (betValue <= player.money) {
                        player.bet = betValue
                        println("Bet set at $$betValue!")
                        betting = false
                    } else println("You don't have enough money for that bet!")
                }
            } catch (e: NumberFormatException){
                println("Invalid input! Try again.")
            }
        }
    }

    override fun runPlayerTurn(player: Player, deck: Deck) {
        println("Your turn, Player ${player.id}! Your hand (${player.hand.value}):")
        println(player.hand)
        var playing = true
        while (playing) {
            Player.printPlayerOptions(mutableListOf("Hit", "Stand", "Quit"))
            when (readLine()) {
                "1" -> {
                    println("Hitting:")
                    player.hand.add(deck.draw())
                    println("Hand is now (${player.hand.value}):")
                    println(player.hand)

                    if (player.hand.value == 31) {
                        println("Hand value is 31, stopping turn.")
                        playing = false
                    } else if (player.hand.value > 31) {
                        println("Hand value is ${player.hand.value}, bust!")
                        player.money -= player.bet
                        players.find { it is Dealer }?.money?.plus(player.bet)
                        player.busted = true
                        if (player.money <= 0) {
                            println("You've ran out of money!")
                            players.remove(player)
                        }
                        playing = false
                    }
                }
                "2" -> {
                    println("Standing:")
                    println("Your hand value is ${player.hand.value}.")
                    playing = false
                }
                "3" -> {
                    println("Thanks for playing!")
                    players.remove(player)
                }
                else -> println("Invalid input! Please try again.")
            }
        }

    }

    override fun runDealerTurn(dealer: Dealer, deck: Deck) {
        println("Dealer's turn!")
        if (dealer.getIsAI()) {
            dealer.autoRunTurn(deck, 27)
            if (dealer.hand.value > 31) {
                println("Dealer has busted!")
                dealer.busted = true
            }
        } else if (dealer.hand.value < 27) {
            var dealerPlaying = true
            while (dealerPlaying) {
                Player.printPlayerOptions(mutableListOf("Hit", "Stand", "Quit"))
                when (readLine()) {
                    "1" -> {
                        dealer.hand.add(deck.draw())
                        println("Hand is now (${dealer.hand.value}):")
                        println(dealer.hand)
                        if (dealer.hand.value > 31) {
                            println("Dealer has busted!")
                            dealer.busted = true
                            dealerPlaying = false
                        } else if (dealer.hand.value >= 27) {
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

    override fun compareHands(players: MutableList<Player>) {
        println("Comparing hands:")

        val dealer = players.find { it is Dealer } as Dealer

        for (player in players) {
            if (player !is Dealer && !player.busted) {
                when {
                    dealer.busted -> {
                        println("Player ${player.id} beats the Dealer!")
                        player.money += player.bet
                        dealer.money -= player.bet
                    }
                    player.hand.isSuit14() -> {
                        println("Player ${player.id} beats the Dealer!")
                        player.money += player.bet
                        dealer.money -= player.bet
                    }
                    dealer.hand.isSuit14() -> {
                        println("Player ${player.id} loses to the Dealer!")
                        dealer.money += player.bet
                        player.money -= player.bet
                    }
                    player.hand.value > dealer.hand.value -> {
                        println("Player ${player.id} beats the Dealer!")
                        player.money += player.bet
                        dealer.money -= player.bet
                    }
                    player.hand.value < dealer.hand.value -> {
                        println("Player ${player.id} loses to the Dealer!")
                        dealer.money += player.bet
                        player.money -= player.bet
                    }
                    player.hand.value == 31 && player.hand.value == dealer.hand.value -> {
                        when {
                            player.hand.isNaturalTriantaEna() -> {
                                println("Player ${player.id} beats the Dealer!")
                                player.money += player.bet
                                dealer.money -= player.bet
                            }
                            dealer.hand.isNaturalTriantaEna() -> {
                                println("Player ${player.id} loses to the Dealer!")
                                dealer.money += player.bet
                                player.money -= player.bet
                            }
                            else -> {
                                println("Player ${player.id} ties with the Dealer! Dealer wins!")
                                dealer.money += player.bet
                                player.money -= player.bet
                            }
                        }
                    }
                    else -> {
                        println("Player ${player.id} ties with the Dealer! Dealer wins!")
                        dealer.money += player.bet
                        player.money -= player.bet
                    }
                }
            }
        }
    }

    private fun rotateDealer(players: MutableList<Player>) {
        val dealer = players.find { it is Dealer } as Dealer
        val potentialDealers = players.asSequence().filter { it !is Dealer && it.money > dealer.money }.sortedBy { it.money }.toList()

        if (potentialDealers.isNotEmpty()) println("Rotating Dealer:")

        for (potentialDealer in potentialDealers) {
            println("Player ${potentialDealer.id}, would you like to be the dealer? (Y/N)")
            var deciding = true
            while (deciding) {
                when (readLine()) {
                    "Y", "y" -> {
                        players.remove(dealer)
                        players.remove(potentialDealer)
                        println("Player ${potentialDealer.id} is now the Dealer! Old Dealer is Player ${dealer.id}.")
                        players.add(Player(dealer.id, dealer.money))
                        players.add(Dealer(false, potentialDealer.id, potentialDealer.money))
                        return
                    }
                    "N", "n" -> {
                        deciding = false
                    }
                    else -> println("Invalid input! Try again!")
                }
            }
        }
    }
}

