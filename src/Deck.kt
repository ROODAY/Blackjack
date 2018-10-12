class Deck(numDecks: Int) {
    private val cards = mutableListOf<Card>()

    init {
        for (d in 1..numDecks) {
            for (suit in Card.suits) {
                for (i in 0..12) {
                    cards.add(Card(suit, i))
                }
            }
        }

        cards.shuffle()
    }

    fun draw(): Card {
        return cards.removeAt(0)
    }
}