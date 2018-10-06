class Deck {
    private val cards = mutableListOf<Card>()

    init {
        for (suit in Card.suits) {
            for (i in 0..12) {
                cards.add(Card(suit, i))
            }
        }
        cards.shuffle()
    }

    fun draw(): Card {
        return cards.removeAt(0)
    }
}