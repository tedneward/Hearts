# Hearts
A design exercise

## Version 1
#### Retro (Written post "Interesting" commit (commit ending in 237abb))
Some of the types I like how things turned out:
* `Card`/`Suit`/`Rank` is working out well. I can treat those pretty much as primitive types without any issues so far.
* `Deck` is also pretty solid. The fluent `remove` call is seamless, and dealing cards was pretty simple with the help of
  the Guava library.
* `View` still needs some work--where in this version I tried to have the views be pretty passive, I think that future
  versions should invert that relationship. Views should drive the action, and Game should be pretty passive, and signal
  to Views by way of return types or exceptions. Overall, though, I could keep going with this approach, if I make some
  finer-grained methods for communicating between `Game` and `View` ("chooseCardToPlay", "chooseCardToPass", etc).
* `Game.Options` is working out well, too, though it does feel like it should be tied more closely to a `Game` instance.
  There's a couple of times where it would've been nice to have that handy instead of having to pull it off of `Trick`.
* `Hand` isn't terrible, but it doesn't feel like it does much for me. May want to ditch it.

## Version 2
#### Proposal (change from v1):
* Fundamental Game/Round/Trick relationships (Game is a series of Rounds, which is a series of Tricks, made up of Plays), 
  all of which should be top-level (non-nested) classes with a reference to its container:
  * Game holds Options, Players and Rounds.
  * Round holds the current pass rotation, and the list of Tricks. Round consists of a deal, pass, and trick play. Rounds
    can calculate the score for the Round (including moonshots).
  * Trick holds the current list of Plays, from which we can get the suit led. When the Trick is complete(?), the Trick can
    calculate the winning Play (and thus the winning Player and Card). Tricks can calculate the score for the Trick.
    (Can Trick calculate the winning Play mid-Trick, a la show who is winning the Trick currently?)
  * Play is a Player/Card pair.
 
* I feel like View should be attached to a Player, for easier per-Player interaction. ConsoleView would just then be
  displaying to the same console all the time, but this would allow more easily for AI players (AIView). OR, the Game
  holds multiple Views, and queries each one for input and only one responds (and that one response is what's used). Is
  there a meaningful difference between a View and a PlayerView? Would I want GhostViews? Hmm.


## Random design thoughts
More message-driven approach? Game is an Engine that receives messages and disperses them to the Trick/Round/Game in that
order for processing or deferral? Thus playing a card is a "playCard" message sent to the Engine, which passes through
the Game, Round, and Trick, before either being accepted or rejected (which implies messages have return values?).

More functional approach? As in, UI is the result of game state passed through a function?

