class Hand(card: Card?) {
    val cards = mutableListOf<Card>()
    val value: Int
        get() {
            var score = 0
            for (card in cards) {
                score += when {
                    card.getNumVal() in 1..9 -> card.getNumVal() + 1
                    card.getNumVal() in 10..12 -> 10
                    else -> 0
                }
            }

            for (card in cards) { // Count aces after other cards so we get the best value possible
                if (card.getNumVal() == 0) {
                    score += when {
                        score + 11 <= 21 -> 11
                        else -> 1
                    }
                }

            }

            return score
        }

    init {
        if (card != null) cards.add(card)
    }

    fun add(card: Card) {
        cards.add(card)
    }

    fun canSplit(): Boolean {
        return cards.size == 2 && cards[0].equals(cards[1])
    }

    fun split(): Pair<Hand, Hand> {
        return Pair(Hand(cards[0]), Hand(cards[1]))
    }

    fun isNaturalBlackjack(): Boolean {
        var nat = false
        if (cards.size == 2) {
            if (cards[0].getNumVal() == 0 && cards[1].getNumVal() in 10..12) nat = true
            else if (cards[1].getNumVal() == 0 && cards[0].getNumVal() in 10..12) nat = true
        }
        return nat
    }

    fun isNaturalTriantaEna(): Boolean {
        return (cards.count {it.getNumVal() == 0} == 1 && cards.count {it.getNumVal() in 10..12} == 2)
    }

    fun isSuit14(): Boolean {
        val suit = cards[0].getSuit()
        return (value == 14 && cards.all {it.getSuit() == suit})
    }

    override fun toString(): String {
        var hand = ""
        for (card in cards) hand += "$card\n"
        return hand
    }
}