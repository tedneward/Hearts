package com.newardassociates.hearts;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A collection of Plays. Each Trick starts with a led card, which determines
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
    public void add(Play play) { plays.add(play); }

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
        return new Iterable<Player>() {
            @Override
            public Iterator<Player> iterator() {
                return new Iterator<Player>() {
                    final int start = playerList.indexOf(leadingPlayer);
                    int count = 0;
                    Player current = playerList.get(start + count);

                    @Override
                    public boolean hasNext() {
                        return count < playerList.size();
                    }

                    @Override
                    public Player next() {
                        Player toBeReturned = current;
                        count++;
                        int next = (start + count) < playerList.size() ? (start + count) : (start + count - playerList.size());
                        current = playerList.get(next);
                        return toBeReturned;
                    }
                };
            }
        };
    }

    public String legalCardToPlay(Play play) {
        logger.entering(Game.Options.class.getCanonicalName(),
                "legalCardToPlay", play);

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

        // Is this the first card in the trick and the card is a heart?
        if ((getPlays().size() == 0) && (play.card.suit == Suit.HEART)) {
            // Have hearts been broken? Can't lead a heart until they have
            if (! getRound().heartsBroken()) {
                logger.info("You cannot lead a Heart until Hearts are broken");
                return "You cannot lead a Heart until Hearts are broken";
            }
        }

        // Is the card of the led suit?
        if ((getPlays().size() > 0) && (play.card.suit != getLedSuit())) {
            // Does the player have a card of that suit?
            for (Card c : play.player.getHand()) {
                if (c != play.card && c.suit == getLedSuit()) {
                    logger.info("You must play a card of the suit led");
                    return "You must play a card of the suit led";
                }
            }
        }

        logger.info(play.card + " is an acceptable play");
        return "";
    }

    public Suit getLedSuit() {
        if (plays.size() > 0)
            return plays.get(0).card.suit;
        else
            return null;
    }

    public Play getWinningPlay() {
        checkArgument(plays.size() == getRound().getGame().getOptions().numberOfPlayers);

        Suit leadSuit = getLedSuit();
        return plays.stream()
                .filter( (play) -> play.card.suit == leadSuit )
                .max( (p1, p2) -> p1.card.rank.ordinal() - p2.card.rank.ordinal() ).get();
    }
    public Player getWinningPlayer() { return getWinningPlay().player; }
}