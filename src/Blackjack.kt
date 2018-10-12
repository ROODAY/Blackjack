import kotlin.system.exitProcess

class Blackjack : CardGame() {

    override fun dealCards(players: MutableList<Player>, deck: Deck, cardsToDeal: Int) {
        for (i in 1..cardsToDeal) {
            for (player in players) {
                player.hand.add(deck.draw())
            }
        }
    }

    override fun runRound(deck: Deck) {
        // Reset players for new round
        for (player in players) {
            player.reset()
        }

        println("Collecting Player Bets:")
        for (player in players.filter { it !is Dealer }) {
            setPlayerBet(player)
        }
        println("\n\n\n\n\n\n\n\n\n\n\n\n")

        println("Dealing Cards:")
        // Deal 2 cards to all players, including the Dealer
        dealCards(players, deck, 2)

        // Show the Dealer's card
        (players.find { it is Dealer } as Dealer).showFirstCard()

        println("Running player turns:")
        for (player in players.filter { it !is Dealer }) {
            runPlayerTurn(player, deck)
        }
        println("\n\n\n\n\n\n\n\n\n\n\n\n")

        println("Dealer's hand (${players.find { it is Dealer }?.hand?.value}):")
        println(players.find { it is Dealer }?.hand)
        runDealerTurn(players.find { it is Dealer } as Dealer, deck)
        println("\n\n\n\n\n\n\n\n\n\n\n\n")

        compareHands(players)
        println("\n\n\n\n\n\n\n\n\n\n\n\n")
    }

    override fun setPlayerBet(player: Player) {
        println("Player ${player.id}, what is your bet? (You currently have $${player.money})")
        var betting = true
        while (betting) {
            try {
                val input = readLine()?.toInt() as Int
                if (input <= player.money) {
                    player.bet = input
                    println("Bet set at $${player.bet}!")
                    betting = false
                } else println("You don't have enough money for that bet!")
            } catch (e: NumberFormatException){
                println("Invalid input! Try again.")
            }
        }
    }

    override fun runPlayerTurn(player: Player, deck: Deck) {
        var handsInterator = player.hands.listIterator()
        hands@while (handsInterator.hasNext()) {
            val hand = handsInterator.next()
            val handIndex = player.hands.indexOf(hand)
            println("Player ${player.id}'s Hand ${handIndex + 1} (${hand.value}):")
            println(hand)

            var playingHand = true
            while (playingHand) {
                Player.printPlayerOptions(mutableListOf("Hit", "Stand", "Split", "Double Up", "Quit"))
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
                            player.money -= player.bet
                            if (player.money <= 0) {
                                println("You've ran out of money!")
                                players.remove(player)
                                break@hands
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
                        if (player.money >= player.bet * 2) {
                            player.bet *= 2
                            println("Bet is now $${player.bet}!")
                            hand.add(deck.draw())
                            println("Hand is now (${hand.value}):")
                            println(hand)

                            if (hand.value > 21) {
                                println("Hand value is ${hand.value}, bust!")
                                println("Player loses $${player.bet}!")
                                player.money -= player.bet
                                if (player.money <= 0) {
                                    println("You've ran out of money!")
                                    players.remove(player)
                                    break@hands
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
                        players.remove(player)
                        break@hands
                    }
                    else -> println("Invalid input! Please try again.")
                }
            }
        }

        player.busted = true
        for (hand in player.hands) {
            if (hand.value <= 21) {
                player.busted = false
                break
            }
        }
    }

    override fun runDealerTurn(dealer: Dealer, deck: Deck) {
        println("Dealer's turn. Dealer's hand (${dealer.hand.value}):")
        println(dealer.hand)

        if (dealer.getIsAI()) {
            dealer.autoRunTurn(deck, 17)
            if (dealer.hand.value > 21) {
                println("Dealer has busted!")
                dealer.busted = true
            }
        } else if (dealer.hand.value < 17) {
            var dealerPlaying = true
            while (dealerPlaying) {
                Player.printPlayerOptions(mutableListOf("Hit", "Stand", "Quit"))
                when (readLine()) {
                    "1" -> {
                        dealer.hand.add(deck.draw())
                        println("Hand is now (${dealer.hand.value}):")
                        println(dealer.hand)
                        if (dealer.hand.value > 21) {
                            println("Dealer has busted!")
                            dealer.busted = true
                            dealerPlaying = false
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

    override fun compareHands(players: MutableList<Player>) {
        println("Comparing hands:")

        val dealer = players.find { it is Dealer } as Dealer

        for (player in players.filter { it !is Dealer }) {
            println("Checking Player ${player.id}'s hands:")
            if (player.busted) {
                println("Player ${player.id} busted all hands!")
                continue
            }
            for ((index, hand) in player.hands.iterator().withIndex()) {
                if (hand.value > 21) {
                    println("Hand ${index + 1} busted, skipping.")
                    continue
                }
                when {
                    dealer.busted -> {
                        println("Hand ${index + 1} beats the dealer! Player wins $${player.bet * 2}!")
                        player.money += player.bet * 2
                    }
                    hand.value > dealer.hand.value -> {
                        println("Hand ${index + 1} beats the dealer! Player wins $${player.bet * 2}!")
                        player.money += player.bet * 2
                    }
                    hand.value < dealer.hand.value -> {
                        println("Hand ${index + 1} loses to the dealer! Player loses $${player.bet}!")
                        player.money -= player.bet
                    }
                    hand.value == 21 && hand.value == dealer.hand.value -> {
                        when {
                            hand.isNaturalBlackjack() -> {
                                println("Hand ${index + 1} beats the dealer! Player wins $${player.bet * 2}!")
                                player.money += player.bet * 2
                            }
                            dealer.hand.isNaturalBlackjack() -> {
                                println("Hand ${index + 1} loses to the dealer! Player loses $${player.bet}!")
                                player.money -= player.bet
                            }
                            else -> println("Hand ${index + 1} results in a draw!")
                        }
                    }
                    else -> println("Hand ${index + 1} results in a draw!")
                }
            }
        }
    }
}