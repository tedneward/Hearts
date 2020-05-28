package com.newardassociates.hearts;

import com.newardassociates.hearts.util.Pair;
import com.newardassociates.hearts.util.Reason;

import java.util.*;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.*;

public class Game {
    private final Logger logger = Logger.getLogger("Game");

    private View view;
    private final List<Player> players = new ArrayList<>();
    private Options options;

    private final List<Round> rounds = new ArrayList<>();

    public static Iterable<Player> turnOrder(List<Player> playerList, Player startingPlayer) {
        return new Iterable<Player>() {
            @Override
            public Iterator<Player> iterator() {
                return new Iterator<Player>() {
                    final int start = playerList.indexOf(startingPlayer);
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

    public static class Options {
        public int numberOfPlayers = 4;
        public List<String> playerNames = new ArrayList<>();
        public int winThreshold = 100;
        public boolean shootingAdds26ToOthers = true;
        public boolean jackOfDiamondsSubtractsTen = false;
        public boolean queenOfSpadesIsAHeart = true;
        public boolean bloodOnFirstRound = false;
        public boolean hittingThresholdExactlyResetsToZero = false;

        /**
         * Based on the number of players, what card do we lead with?
         * @return The Card to start (2C, 3C, etc)
         */
        public Card getStartingCard() {
            checkArgument(numberOfPlayers >= 3, "Should never have less than 3 players");
            checkArgument(numberOfPlayers <= 6, "Should never have more than 6 players");

            switch (numberOfPlayers) {
                case 3: return Card.ThreeClubs;
                case 4: return Card.TwoClubs;
                case 5: return Card.ThreeClubs;
                case 6: return Card.FourClubs;
            }
            throw new IllegalArgumentException("Should never have <3 or >6 players");
        }

        /**
         * Check the validity of this card from this player for this trick.
         *
         * @param trick The current trick
         * @param player The player proposing the play
         * @param card The card the player chose to play
         * @return True if this is legal, false otherwise
         */
        public Reason legalCardToPlay(Round round, Trick trick, Player player, Card card) {
            trick.getGame().logger.entering(Options.class.getCanonicalName(),
                    "legalCardToPlay",
                    new Object[] { round, trick, player, card});

            // Is this the first trick?
            if (round.tricks.size() == 0) {
                // Can points be played on the first round?
                if ((card.suit == Suit.HEART) && (!bloodOnFirstRound)) {
                    trick.getGame().logger.info("You cannot spill blood on the first round");
                    return new Reason(false, "You cannot spill blood on the first round");
                }
                if (card == Card.QueenSpades && (!bloodOnFirstRound) && (queenOfSpadesIsAHeart)) {
                    trick.getGame().logger.info("You cannot play points on the first round--Queen is a heart");
                    return new Reason(false, "You cannot play points on the first round");
                }
            }

            // Is this the first card in the trick and the card is a heart?
            if ((trick.plays.size() == 0) && (card.suit == Suit.HEART)) {
                // Have hearts been broken? Can't lead a heart until they have
                if (! round.heartsBroken()) {
                    trick.getGame().logger.info("You cannot lead a Heart until Hearts are broken");
                    return new Reason(false, "You cannot lead a Heart until Hearts are broken");
                }
            }

            // Is the card of the led suit?
            if ((trick.plays.size() > 0) && (card.suit != trick.getLeadingSuit())) {
                // Does the player have a card of that suit?
                for (Card c : player.getHand()) {
                    if (c != card && c.suit == trick.getLeadingSuit()) {
                        trick.getGame().logger.info("You must play a card of the suit lead");
                        return new Reason(false, "You must play a card of the suit lead");
                    }
                }
            }

            trick.getGame().logger.info(card + " is an acceptable play");
            return new Reason(true, "");
        }
        // Add methods that handle the different options directly;
        // let's try to encapsulate the rules-variations here
    }

    public Game() { }

    public void attachView(View view) {
        // Should I check for null before assignment? Later allow for composite views by creating
        // a CompositeView class that tees everything to multiple views?
        this.view = view;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Player getPlayerForName(String name) {
        for (Player player : players) {
            if (player.getName().equals(name))
                return player;
        }
        return null;
    }

    // For each scoring round: deal cards, pass, player with the two (or three or four) of clubs leads,
    // play trick (each player plays a card, beginning with whomever took last trick) until all cards are played,
    // score, check for win threshold

    public boolean prepare() {
        logger.entering(Game.class.getCanonicalName(), "prepare");

        if (options == null) {
            logger.info("Getting options from view");
            options = view.getOptions();
            logger.info("options chosen: " + options);
        }

        if (options.numberOfPlayers < 3) {
            view.display("Sorry, can't play with less than three players!");
            return false;
        }
        if (options.numberOfPlayers > 6) {
            view.display("Sorry, can't play with more than six players!");
            return false;
        }

        if (options.playerNames.size() != options.numberOfPlayers) {
            view.display("We don't have names for all " + options.playerNames.size() + "players: " +
                    options.playerNames);
            return false;
        }

        for (int i=0; i<options.numberOfPlayers; i++) {
            players.add(new Player(options.playerNames.get(i)));
        }

        return true;
    }

    public class Round {
        public List<Trick> tricks = new ArrayList<>();
        public Map<Player, Integer> scores = new HashMap<>();

        public Round() {
            for (Player p : Game.this.getPlayers()) {
                scores.put(p, 0);
            }
        }

        public boolean heartsBroken() {
            for (Trick t : tricks) {
                for (Pair<Player, Card> play : t.plays) {
                    if (play.second == Card.QueenSpades && options.queenOfSpadesIsAHeart) {
                        return true;
                    }
                    if (play.second.suit == Suit.HEART) {
                        return true;
                    }
                }
            }
            return false;
        }

        public void add(Trick trick) { tricks.add(trick); }
        public Trick lastTrick() { return tricks.get(tricks.size() - 1); }

        public Map<Player, Integer> scoreRound() {
            for (Trick t : tricks) {
                Player winner = t.getWinningPlayer();
                Integer score = t.getScore();
                scores.put(winner, score + scores.get(winner));
            }

            // Did one Player take all the points?

            return scores;
        }
        public int scoreForPlayer(Player p) { return scores.get(p); }
    }

    public Round playRound() {
        Round round = new Round();

        dealCards();
        //passCards();

        Player leadingPlayer = null;
        while (getPlayers().get(0).getHand().handSize() > 0) {
            Game.Trick trick = playTrick(round);
            leadingPlayer = trick.getWinningPlayer();
            leadingPlayer.add(trick);

            round.add(trick);
        }

        rounds.add(round);
        return round;
    }

    public void dealCards() {
        Deck deck = new Deck();
        switch (options.numberOfPlayers) {
            case 3: deck.remove(Card.TwoClubs);
            case 5: deck.remove(Card.TwoClubs).remove(Card.TwoDiamonds);
            case 6: deck.remove(Card.TwoClubs).remove(Card.TwoDiamonds).remove(Card.ThreeClubs).remove(Card.ThreeDiamonds);
        };

        deck.shuffle();

        List<Hand> hands = deck.dealHands(options.numberOfPlayers);
        for (int i=0; i<options.numberOfPlayers; i++) {
            players.get(i).setHand(hands.get(i));
            view.display(players.get(i));
        }
    }

    /**
     * Passing operates in a rotational fashion, passing N cards to a player Y seats away from you,
     * where both N and Y are dependent on the number of players playing.
     */
    public void passCards() {

    }

    public class Trick {
        public List<Pair<Player, Card>> plays = new ArrayList<>(4);

        // This is a little hack to get the Game reference associated with this Trick
        public Game getGame() { return Game.this; }

        public void play(Player player, Card card) {
            plays.add(new Pair<>(player, card));
        }

        public Player getLeadingPlayer() {
            checkArgument(plays.size() > 0);
            return plays.get(0).first;
        }

        public Suit getLeadingSuit() {
            checkArgument(plays.size() > 0);
            return plays.get(0).second.suit;
        }

        public Pair<Player, Card> getWinningPlay() {
            checkArgument(plays.size() == options.numberOfPlayers);

            Suit leadSuit = getLeadingSuit();
            return plays.stream()
                    .filter( (pair) -> pair.second.suit == leadSuit )
                    .max( (c1, c2) -> c1.second.rank.ordinal() - c2.second.rank.ordinal() ).get();
        }

        public Player getWinningPlayer() {
            return getWinningPlay().first;
        }

        public Card getWinningCard() {
            return getWinningPlay().second;
        }

        public int getScore() {
            int score = 0;

            for (Pair<Player, Card> play : plays) {
                Card card = play.second;
                if (card.suit == Suit.HEART) {
                    score++;
                }

                if (card == Card.QueenSpades) {
                    score += 13;
                }

                if (card == Card.JackDiamonds && Game.this.options.jackOfDiamondsSubtractsTen) {
                    score -= 10;
                }
            }

            return score;
        }
    }

    public Trick playTrick(Round round) {
        Trick currentTrick = new Trick();

        Player leadingPlayer;
        if (round.tricks.size() == 0) {
            // This is the first trick--the leading player is the one with the appropriate card
            leadingPlayer = findPlayerWith(options.getStartingCard());
            view.display(leadingPlayer.getName() + " must lead with the " + options.getStartingCard());
        }
        else {
            leadingPlayer = round.tricks.get(round.tricks.size() - 1).getWinningPlayer();
            view.display(leadingPlayer.getName() + " leads");
        }

        // Rotate through the list of Players, all the way around to the start
        // of the List if necessary (which it often will be); each Player gets
        // to play one Card
        for (Player player : turnOrder(players, leadingPlayer)) {
            while (true) {
                Card chosenCard = view.chooseCard(player);

                // verify chosen card is in the player's hand, just to be double-certain
                if (! player.getHand().contains(chosenCard)) {
                    view.display("That card is not in your hand!");
                    continue;
                }

                // verify chosen card is legal
                Reason reason = options.legalCardToPlay(round, currentTrick, player, chosenCard);
                if (!reason.result) {
                    view.display("That card is not legal to play: " + reason.rationale);
                }
                else {
                    currentTrick.play(player, chosenCard);
                    break;
                }
            }
        }

        return currentTrick;
    }

    public boolean gameOver() {
        return false;
    }

    public Map<Player, Integer> getScores() {
        return Collections.emptyMap();
    }


    private Player findPlayerWith(Card card) {
        for (Player player : players) {
            if (player.getHand().toString().contains(card.toString()))
                return player;
        }
        throw new IllegalArgumentException("Tried to find a card that wasn't in a player's hand");
    }
}
