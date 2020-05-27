package com.newardassociates.hearts;

import java.util.*;

import static com.google.common.base.Preconditions.*;

public class Game {
    private View view;
    private List<Player> players = new ArrayList<>();
    private Deck deck;
    private Options options;

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
        // Add methods that handle the different options directly;
        // let's try to encapsulate the rules-variations here
    }

    public Game() { }

    public void attachView(View view) {
        // Later allow for composite views by creating a CompositeView class that tees everything
        // to multiple views?
        // Should I check for null before assignment?
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
        if (options == null)
            options = view.getOptions();

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

    public void dealCards() {
        Deck deck = new Deck();
        switch (options.numberOfPlayers) {
            case 3:
                deck = deck.remove(Card.TwoClubs);
                break;
            case 5:
                deck = deck.remove(Card.TwoClubs).remove(Card.TwoDiamonds);
                break;
            case 6:
                deck = deck.remove(Card.TwoClubs).remove(Card.TwoDiamonds).remove(Card.ThreeClubs).remove(Card.ThreeDiamonds);
                break;
        }

        deck.shuffle();

        List<Hand> hands = deck.dealHands(options.numberOfPlayers);
        for (int i=0; i<options.numberOfPlayers; i++) {
            players.get(i).setHand(hands.get(i));
            view.display(players.get(i));
        }
    }

    public class Trick {
        public List<AbstractMap.SimpleEntry<Player, Card>> plays = new ArrayList<>(4);

        public void play(Player player, Card card) {
            plays.add(new AbstractMap.SimpleEntry<>(player, card));
        }

        public Player getLeadingPlayer() {
            checkArgument(plays.size() > 0);
            return plays.get(0).getKey();
        }

        public Suit getLeadingSuit() {
            checkArgument(plays.size() > 0);
            return plays.get(0).getValue().suit;
        }

        public AbstractMap.SimpleEntry<Player, Card> getWinningPlay() {
            checkArgument(plays.size() == options.numberOfPlayers);

            Suit leadSuit = getLeadingSuit();
            return plays.stream()
                    .filter( (pair) -> pair.getValue().suit == leadSuit )
                    .max( (c1, c2) -> c1.getValue().rank.ordinal() - c2.getValue().rank.ordinal() ).get();
        }

        public Player getWinningPlayer() {
            return getWinningPlay().getKey();
        }

        public Card getWinningCard() {
            return getWinningPlay().getValue();
        }

        public int getScore() {
            int score = 0;

            for (AbstractMap.SimpleEntry<Player, Card> play : plays) {
                Card card = play.getValue();
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

    public Trick playTrick(Player leadingPlayer) {
        Trick trick = new Trick();

        if (leadingPlayer == null) {
            // This is the first trick--the leading player is the one with the appropriate card
            leadingPlayer = findPlayerWith(options.getStartingCard());
            view.display(leadingPlayer.getName() + " must lead with the " + options.getStartingCard());
        }

        // Rotate through the list of Players, all the way around to the start
        // of the List if necessary (which it often will be); each Player gets
        // to play one Card
        for (Player player : turnOrder(players, leadingPlayer)) {
            Card chosenCard = view.chooseCard(player);

            // verify chosen card is legal

            trick.play(player, chosenCard);
        }

        return trick; //FIXME
    }

    private Player findPlayerWith(Card card) {
        for (Player player : players) {
            if (player.getHand().toString().contains(card.toString()))
                return player;
        }
        throw new IllegalArgumentException("Tried to find a card that wasn't in a player's hand");
    }
}
