package com.newardassociates.hearts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Round is a full scoring round of Tricks, consisting of a deal, a pass, and
 * play until all cards are played. Each Round will then have a score associated
 * with it once finished. Tracks in-round state such as whether Hearts have been
 * broken or not.
 */
public class Round {
    private final Game game;
    private final List<Trick> tricks = new ArrayList<>();
    private final Deck deck;
    private final Card startingCard;

    public Round(Game game, /*Pass pass,*/ Deck deck, Card startingCard) {
        this.game = game;
        this.deck = deck;
        this.startingCard = startingCard;
    }

    public Game getGame() { return game; }

    public boolean heartsBroken() {
        return false;
    }

    public List<Trick> getTricks() { return Collections.unmodifiableList(tricks); }

    public boolean over() { return tricks.size() == getGame().getHandSize(); }
}
