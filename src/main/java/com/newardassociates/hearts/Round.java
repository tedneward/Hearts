package com.newardassociates.hearts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * A Round is a full scoring round of Tricks, consisting of a deal, a pass, and
 * play until all cards are played. Each Round will then have a score associated
 * with it once finished. Tracks in-round state such as whether Hearts have been
 * broken or not.
 */
public class Round {
    private final Game game;
    private final List<Trick> tricks = new ArrayList<>();
    private Trick currentTrick;
    private Deck deck;
    private Card startingCard;

    public Round(Game game /*, Pass pass*/) {
        this.game = game;
    }

    public Game getGame() { return game; }
    public void setDeck(Deck deck) { this.deck = deck; }
    public void setStartingCard(Card card) { this.startingCard = card; }

    /**
     * Have hearts been broken yet this round?
     *
     * @return true or false
     */
    public boolean heartsBroken() {
        // Any of the previous Tricks have points in it?
        for (Trick t : tricks)
            if (t.getPlays().stream().anyMatch(POINTS))
                return true;

        // Check the current Trick, might have been broken in play
        return currentTrick.getPlays().stream().anyMatch(POINTS);
    }
    private final Predicate<Play> POINTS =
            (play) -> (play.card == Card.QueenSpades && getGame().getOptions().queenOfSpadesIsAHeart) ||
                    play.card.suit == Suit.HEART;

    /**
     * Get an unmodifiable list of the Tricks that been played this Round so far.
     * @return
     */
    public List<Trick> getTricks() { return Collections.unmodifiableList(tricks); }

    /**
     * Is the Round over?
     * @return
     */
    public boolean over() { return tricks.size() == getGame().getHandSize(); }

    /**
     * Deal out Cards to each of the Players from the Deck.
     */
    public void dealHands() {
        List<Hand> hands = deck.dealHands(getGame().getOptions().numberOfPlayers);
        for (int i=0; i<getGame().getOptions().numberOfPlayers; i++) {
            getGame().getPlayers().get(i).setHand(hands.get(i));
        }
    }

    /**
     * Begin a Trick. Assumes hands have been dealt.
     *
     * @return The current Trick, which is not part of the list of Tricks yet for the Round.
     */
    public Trick beginTrick() {
        Trick trick = new Trick(this);

        // Is this the first trick? Set the starting player in that case
        if (tricks.size() == 0) {
            Player startingPlayer =
                    game.getPlayers().stream().
                            filter( (player) -> player.getHand().contains(startingCard)).
                            findFirst().orElse(null);
            trick.setLeadingPlayer(startingPlayer);
        }
        else {
            // Nope, set it to the winner of the last Trick
            Trick lastTrick = tricks.get(tricks.size() - 1);
            trick.setLeadingPlayer(lastTrick.getWinningPlayer());
        }

        currentTrick = trick;
        return trick;
    }

    /**
     * Ends the current Trick and adds it to the list of finished Tricks.
     *
     * @return Whether this Round is over (all cards played).
     */
    public boolean endTrick() {
        tricks.add(currentTrick);
        return (over());
    }

    // score the Round
}
