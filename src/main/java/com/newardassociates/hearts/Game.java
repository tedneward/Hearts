package com.newardassociates.hearts;

import java.util.*;
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

        logger.info("Constructing Game using " + options);

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
            case 3: deck.remove(Card.TwoClubs); break;
            case 5: deck.remove(Card.TwoClubs).remove(Card.TwoDiamonds); break;
            case 6: deck.remove(Card.TwoClubs).remove(Card.TwoDiamonds).remove(Card.ThreeClubs).remove(Card.ThreeDiamonds); break;
        }
        if (shuffle)
            deck.shuffle();
        return deck;
    }
    public Deck getDeck() { return getDeck(true); }

    public Round beginRound() {
        checkArgument(currentRound == null, "Cannot begin a Round when one is already in progress!");
        logger.info("Beginning new Round");

        currentRound = new Round(this);
        currentRound.setDeck(getDeck());
        currentRound.setStartingCard(getStartingCard());
        return currentRound;
    }
    public Round getCurrentRound() { return currentRound; }
    public void endRound() {
        checkArgument(currentRound != null, "Cannot end a Round when one hasn't been started!");
        logger.info("Ending round");

        rounds.add(currentRound);
        currentRound = null;
    }

    public Map<Player, Integer> calculateScores() {
        Map<Player, Integer> scores = new HashMap<>();
        for (Round round : rounds) {
            for (Player player : round.score().keySet()) {
                scores.put(player, scores.get(player) + round.score().get(player));

                // Reset at winThreshold exactly if that option is turned on
                if (options.hittingThresholdExactlyResetsToZero &&
                        (scores.get(player) == options.winThreshold)) {
                    scores.put(player, 0);
                }
            }
        }
        return scores;
    }

    public boolean over() {
        Map<Player, Integer> scores = calculateScores();
        for (Player player : players) {
            if (scores.get(player) > options.winThreshold)
                return true;
        }
        return false;
    }
}
