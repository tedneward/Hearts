package com.newardassociates.hearts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.*;

/**
 * Game holds Options, Players and Rounds.
 */
public class Game {
    private final Logger logger = Logger.getLogger("Game");

    private final List<Round> rounds = new ArrayList<>();
    private Round currentRound = null;
    private final List<Player> players = new ArrayList<>();
    private final Options options;

    /**
     * These are the options used to construct a Game.
     */
    public static class Options {
        public int numberOfPlayers = 4;
        public List<String> playerNames = new ArrayList<>(6);
        public int winThreshold = 100;
        public boolean shootingAdds26ToOthers = true;
        public boolean jackOfDiamondsSubtractsTen = false;
        public boolean queenOfSpadesIsAHeart = true;
        public boolean bloodOnFirstRound = false;
        public boolean hittingThresholdExactlyResetsToZero = false;
    }

    public Game(Options options) {
        checkArgument(options.numberOfPlayers >= 3, "Cannot play with less than 3 players");
        checkArgument(options.numberOfPlayers <= 6, "Cannot play with more than 6 players");
        checkArgument(options.playerNames.size() == options.numberOfPlayers,
                "We don't have names for all of the players! " +
                        "We have " + options.numberOfPlayers + " players " +
                        "and the following names: " + options.playerNames);

        for (String name : options.playerNames) {
            players.add(new Player(name));
        }
        this.options = options;
    }

    public Options getOptions() { return options; }
    public List<Player> getPlayers() { return Collections.unmodifiableList(players); }
    public List<Round> getRounds() { return Collections.unmodifiableList(rounds); }

    public Player getPlayerFromName(String name) {
        Optional<Player> result =
                players.stream().filter( (player) -> player.getName().equals(name) ).findFirst();
        return result.orElse(null);
    }

    /**
     * Based on the number of players, what card do we lead with?
     * @return The Card to start (2C, 3C, etc)
     */
    public Card getStartingCard() {
        switch (options.numberOfPlayers) {
            case 3: return Card.ThreeClubs;
            case 4: return Card.TwoClubs;
            case 5: return Card.ThreeClubs;
            case 6: return Card.FourClubs;
            default: throw new IllegalArgumentException("Should never have <3 or >6 players");
        }
    }
    public int getHandSize() {
        switch (options.numberOfPlayers) {
            case 3: return 17;
            case 4: return 13;
            case 5: return 10;
            case 6: return 8;
            default: throw new IllegalArgumentException("Should never have <3 or >6 players");
        }
    }
    public Deck getDeck(boolean shuffle) {
        Deck deck = new Deck();
        switch (options.numberOfPlayers) {
            case 3: deck.remove(Card.TwoClubs);
            case 5: deck.remove(Card.TwoClubs).remove(Card.TwoDiamonds);
            case 6: deck.remove(Card.TwoClubs).remove(Card.TwoDiamonds).remove(Card.ThreeClubs).remove(Card.ThreeDiamonds);
        };
        if (shuffle)
            deck.shuffle();
        return deck;
    }
    public Deck getDeck() { return getDeck(true); }

    public Round beginRound() {
        checkArgument(currentRound == null, "Cannot begin a Round when one is already in progress!");

        currentRound = new Round(this, getDeck(), getStartingCard());
        return currentRound;
    }
    public Round getCurrentRound() { return currentRound; }
    public void endRound() {
        checkArgument(currentRound != null, "Cannot end a Round when one hasn't been started!");

        rounds.add(currentRound);
        currentRound = null;
    }
}
