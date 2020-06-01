package com.newardassociates.hearts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A collection of Plays in a Round. Each Trick starts with a led card, which determines
 * the suit that all other players must follow if they can. Tricks always belong
 * to a Round.
 */
public class Trick {
    private final Logger logger = Logger.getLogger("Trick");

    private final Round round;
    private final List<Play> plays = new ArrayList<>();
    private Player leadingPlayer;

    public Trick(Round round) {
        this.round = round;
    }

    public Round getRound() { return round; }
    public List<Play> getPlays() { return Collections.unmodifiableList(plays); }

    /**
     * This adds the Play to the list of Plays for this Trick. This method does NOT check
     * legality of the move (for easier testing); that is deferred to the legalPlay method.
     */
    public void add(Play play) {
        checkArgument(plays.size() < getRound().getGame().getOptions().numberOfPlayers);
        plays.add(play);
    }

    public Player getLeadingPlayer() {
        return leadingPlayer;
    }
    public void setLeadingPlayer(Player player) {
        leadingPlayer = player;
    }

    public Iterable<Player> turnOrder() {
        checkArgument(leadingPlayer != null,
                "Cannot resolve turn order without knowing who the leadingPlayer is!");

        List<Player> playerList = round.getGame().getPlayers();
        return Stream.generate(playerList::stream).flatMap(s -> s)   //cycle infinitely
                .skip(playerList.indexOf(leadingPlayer))             //skip the players before the leader
                .limit(getRound().getGame().getOptions().numberOfPlayers)::iterator; // limit to the number of players
    }

    /**
     * Is this a legal card to play, given the status of the Trick and the Round?
     */
    public String legalCardToPlay(Play play) {
        logger.entering(Game.Options.class.getCanonicalName(),
                "legalCardToPlay", play);

        // Is this the first play of the Trick?
        if (getPlays().size() == 0) {
            // Is this by the player required to lead?
            if (play.player != leadingPlayer) {
                return "Play must start with the player designated to lead";
            }

            // Is this the first play of the first Trick, and is it the required Card?
            if (getRound().getTricks().size() == 0) {
                if (play.card != getRound().getGame().getStartingCard()) {
                    return "You must start with the " + getRound().getGame().getStartingCard().toString();
                }
            }

            // Have hearts been broken? Can't lead a heart until they have
            if (play.card.suit == Suit.HEART && (! getRound().heartsBroken())) {
                return "You cannot lead a Heart until Hearts are broken";
            }
        }
        else {
            // Is the card of the led suit?
            if (play.card.suit != getLedSuit()) {
                // Does the player have a card of that suit?
                for (Card c : play.player.getHand()) {
                    if (c != play.card && c.suit == getLedSuit()) {
                        logger.info("You must play a card of the suit led");
                        return "You must play a card of the suit led";
                    }
                }
            }
        }

        // Is this the first trick?
        if (getRound().getTricks().size() == 0) {
            // Can points be played on the first round?
            if ((play.card.suit == Suit.HEART) && (!getRound().getGame().getOptions().bloodOnFirstRound)) {
                logger.info("Cannot play points on the first round");
                return "Cannot play points on the first round";
            }
            if (play.card == Card.QueenSpades && (!getRound().getGame().getOptions().bloodOnFirstRound) &&
                    (getRound().getGame().getOptions().queenOfSpadesIsAHeart)) {
                logger.info("Cannot play points on the first round--Queen is a heart");
                return "Cannot play points on the first round";
            }
        }

        logger.info(play.card + " is an acceptable play");
        return "";
    }

    /**
     * Can be called at any time to find the suit that was led.
     * @return null if nothing has been played yet, or else the suit that was led.
     */
    public Suit getLedSuit() {
        return (plays.size() > 0) ? plays.get(0).card.suit : null;
    }

    /**
     * Can be called at any time to find the current high card and the Player that played it.
     * @return the winning play, or null if no cards have been played yet.
     */
    public Play getHighPlay() {
        Suit leadSuit = getLedSuit();
        return plays.stream()
                .filter( (play) -> play.card.suit == leadSuit )
                .max( Play::compareRank ).orElse(null);
    }

    /**
     * Can be called only when the Trick is complete to find the Play that won the Trick.
     * @return The winning Play.
     */
    public Play getWinningPlay() {
        checkArgument(plays.size() == getRound().getGame().getOptions().numberOfPlayers);
        return getHighPlay();
    }

    /**
     * Can be called only when the Trick is complete to find the Player who won the Trick.
     * @return The winning Player.
     */
    public Player getWinningPlayer() { return getWinningPlay().player; }

    /**
     * Can be called at any time to find the current score value of the Trick.
     * Does NOT count the Jack--that is calculated as part of the Round. (Much
     * easier to calculate shooting if we don't include the Jack here.)
     * @return The score.
     */
    public int getScore() {
        int score = 0;
        for (Play play : plays) {
            Card card = play.card;
            if (card.suit == Suit.HEART) {
                score++;
            }

            if (card == Card.QueenSpades) {
                score += 13;
            }
        }
        return score;
    }
}
