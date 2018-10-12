# Blackjack
Implementation of Blackjack in Kotlin for CS 591 D1

# How to run
In IntelliJ, simply run the Blackjack.kt file. To run in command line,
run `java -jar out/artifacts/Blackjack_jar/Blackjack.jar`.

# Object Design Specification

## Blackjack

Contains main game logic. Uses the following global variables:
```
players // List of Player and Dealer
playerBusted // Did the player bust this round
dealerBusted // Did the dealer bust this round
bet // Player's bet
```
Implements the following functions:  
`main`: Main game loop. Runs `initPlayers` then a loop of `runRound `
until the game is over. 

`initPlayers`: Prompts for number of players. If 1, creates an AI Dealer,
if 2, creates a playable Dealer (the other player is the normal
Player).  

`runRound`: Controls a round of gameplay. Resets the hands of the
Player and Dealer and creates a new Deck. Then prompts user for 
their bet and deals cards to Player and Dealer. Then shows the 
Player the Dealer's first card, and runs the following in order:
`runPlayerTurn`, `runDealerTurn`, and `compareHands`.  

`runPlayerTurn`: Biggest function, controls logic of Player turn. 
Creates an iterator on the Player's hands (plural because of the
case where Player splits a hand), and runs an input loop on each
hand. For each hand, the player is shown their hand and their value,
and prompted to 1. Hit, 2. Stand, 3. Split, 4. Double Up, or 5. Quit.
Those options are implemented according to the assignment handout. 
Only thing of note is the fact that hands are looped over using
an iterator, allowing new hands (in case of a split) to be added
into the iterator while in the process of iterating. After player 
has made choices for each hand, a loop checks to make sure not
all hands have busted (in which case we don't have to bother running
the Dealer turn). If at least one hand didn't bust, set `playerBusted`
to false and continue. 

`runDealerTurn`: Runs the Dealer logic. If Dealer is an AI, just
run the `autoRunTurn` function in the Dealer class. Else, present
options similar to the player: 1. Hit, 2. Stand, 3. Quit. They are 
implemented according to the assignment handout. If the dealer doesn't
bust, make sure `dealerBusted` is false and continue.

`compareHands`: Compares Player hands to the Dealer's and awards
bets as required. If a Player hand is greater, the Player gets
double the bet money for that hand. If it's worse, the Player
loses the bet money for that hand. If it's a draw, nothing happens.


## Card
Keeps track of data for individual cards. Has the following private
instance variables (required in constructor):
```
suit // String of suit name
numVal // int of card value (0 - 12)
```

Has the following static class variables:
```
suits // Arrays of possible suits (Strings)
readableNames // Array of readable names (card values are stored as ints)
```

Implements the following functions:  
`getNumVal`: Returns the value of the private numVal 

`equals`: Helper to compare two cards (checks equality of card's numVal) 

`toString`: Prints card in form "Value of Suit", e.g. "Ace of Hearts"  

## Hand
Keeps track of a particular hand, contains instances of the Card class.
Takes in a Card optional in the constructor, which if exists will
be added to the private list of cards. Has following private instance
variables:
```
cards // List of cards in hand
value // Value of the hand, computed every time hand.value is called
```

On init, if a Card was passed to constructor, it is added to the 
`cards` list.

Implements the following functions:  
`add`: Takes a Card and adds it to the `cards` list. 

`canSplit`: Returns true if the Hand can be split (if only
two cards are in the hand and they have the same `numVal`). 

`split`: Returns a Pair of the two Cards in the Hand. 

`isNaturalBlackjack`: Returns true if the Hand consists of only
two Cards, an Ace and a face card. 

`toString`: Override to allow direct printing of a Hand, in which
each newline is a printed Card. 

## Deck
Keeps track of a deck of 52 cards. A new one is generated for each
round in Blackjack.kt. Has an empty constructor, on init populates
list of cards with a standard set of 52 cards and shuffles them. 
Maintains the following private instance variable:
```
cards // Mutable list of cards in the deck
```

Implements the following function:  
`draw`: Takes the first (top) card of the deck, removes it from
the deck, and returns it.

## Player
Keeps track of the data each Player (and Dealer) needs. Has the
following private instance variables (and uses an empty constructor): 
```
money // Only needed for Player, once 0 game is over
hands // Mutable list of hands (since a player can split)
hand // Shorthand for first hand in list
```

Also has the following static class function:
```
printPlayerOptions // Prints the options for Players as described in the Blackjack class
```

Implements the following function:  
`resetHands`: Clears the Player's hands and adds an empty Hand.
Used when starting a new round. 

## Dealer
Extends the Player class with a constructor to set if computer
controlled or not. Has no new private instance variables, but inherits
those of Player. Has the following static class function:
```
printDealerOptions // Similar to the Player one, prints options as described in Blackjack class
```

Implements the following functions:   
`showFirstCard`: Shows the first card of the Dealer hand (when 
the Player starts their turn).

`getIsAI`: Returns if the Dealer is computer controlled or not (
Blackjack uses this to decide whether to auto run turn).   

`autoRunTurn`: Simply just "hits" until the Dealer's hand is equal
to or greater than 17 (or if the Dealer busts).