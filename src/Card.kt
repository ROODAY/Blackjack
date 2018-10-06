class Card(private val suit: String, private val numVal: Int) {

    fun getNumVal(): Int {
        return numVal
    }

    fun equals(other: Card): Boolean {
        return numVal == other.numVal
    }

    override fun toString(): String {
       return "${Card.readableNames[numVal]} of $suit"
    }

    companion object {
        val suits = arrayOf("Clubs","Diamonds","Spades","Hearts")
        val readableNames = arrayOf("Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King")
    }
}