# Changes to Object Model

- First main thing was abstracting Blackjack into a specific class
instead of running it as the main class. This was so that both
games could be played from a single driver class. This change also
allows other variations to be implemented and easily added to the 
driver in the future.
- Now that Blackjack and Trianta Ena each have their own class, it
made sense to make an abstract parent class CardGame that both gammes could
inherit from. 
   - CardGame provides the players variable, which is a list of
   all players, including the Dealer. The class also specifies two
   functions, initPlayers and runGame. The former asks how many
   players are playing (if only 1, an AI Dealer is added). The 
   latter runs a loop of the abstract method runRound. 
   - CardGame has 6 abstract functions that inheritors must implement:
   runRound, dealCards, setPlayerBet, runPlayerTurn, runDealerTurn, and
   compareHands. These are the functions I identified to be common
   between all variations of Blackjack.
- Since Blackjack was the original game, it was the easiest to port
over to this object model, as all of its logic fit nicely within
the abstract functions. Trianta Ena required an extra function on
top of the abstract ones, rotateDealer, which would give players
with more money than the Dealer the option of taking the role.
- Outside of the main game classes, every other class was also touched,
some in more minor ways than others, to accommodate this new inheritance
setup.
    - The Card class got a new helper function, getSuit, since suit is a private variable
    - The Player class got a constructor for ID and starting money,
    as well as new variables bet, folded, and busted. This allowed
    players to be differentiated easier, and keep track of themselves
    instead of relying on the game logic to do that. The player also
    has a new reset function instead of resetHands, as there are more
    variables to reset now for each round.
        - The Dealer class also got these changes as it inherits from
        Player
    - The static Player.printPlayerOptions now takes in a list of options
    to be printed, allowing it be used for games with different player
    actions.
- The Hand class got new helper functions to check if a hand was a
natural Trianta Ena or a 14 value of the same suit.
- The Deck class was given a constructor for number of decks to use,
so games like Trianta Ena can request a deck with twice as many cards.

# Review of Changes

All these changes, while not necessarily making the implementation
easier, definitely made it better and cleaner. Having to write
the CardGame class made me think about what parts of Blackjack and
Trianta Ena are common and can be abstracted away, and the way 
the code is written now allows for new variations to easily be
added. In retrospect, it would have been a good idea to do this
from the start, at least just to see what "sections" make up
Blackjack. Also, the new implementation made it easy to have many
players, as originally it only supported 1 or 2 players. Now it
caps at 9, but there is nothing stopping the game from having more.