package com.newardassociates.hearts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrickTests {
    Game game;
    Round round;
    Trick trick;

    Trick setupTrick(int numPlayers, boolean jack) {
        Game.Options options = new Game.Options();
        options.numberOfPlayers = numPlayers;
        options.playerNames.add("Ted");
        options.playerNames.add("Char");
        options.playerNames.add("Mike");
        if (numPlayers > 3)
            options.playerNames.add("Matt");
        if (numPlayers > 4)
            options.playerNames.add("Vincent");
        options.jackOfDiamondsSubtractsTen = jack;
        game = new Game(options);
        round = game.beginRound();
        round.setDeck(game.getDeck(false)); // No shuffle
        round.dealHands();
        trick = round.beginTrick();
        return trick;
    }
    Trick setupTrick(int numPlayers) { return setupTrick(numPlayers, false); }

    @Test public void turnRotationStartingFromPlayerZero() {
        setupTrick(4).setLeadingPlayer(game.getPlayerFromName("Ted"));

        int tedSeen = -1, charSeen = -1, mikeSeen = -1, mattSeen = -1;
        int loopCount = 0;
        for (Player player : trick.turnOrder()) {
            switch (player.getName()) {
                case "Ted": tedSeen = loopCount; break;
                case "Char": charSeen = loopCount; break;
                case "Mike": mikeSeen = loopCount; break;
                case "Matt": mattSeen = loopCount; break;
            }
            ++loopCount;
        }

        assertEquals(4, loopCount); // We should only have gone through the loop 4 times

        // This is the order they should've been seen in
        assertEquals(0, tedSeen);
        assertEquals(1, charSeen);
        assertEquals(2, mikeSeen);
        assertEquals(3, mattSeen);
    }

    @Test public void turnRotationStartingFromPlayerTwo() {
        setupTrick(4).setLeadingPlayer(game.getPlayerFromName("Mike"));

        int tedSeen = -1, charSeen = -1, mikeSeen = -1, mattSeen = -1;
        int loopCount = 0;
        for (Player player : trick.turnOrder()) {
            switch (player.getName()) {
                case "Ted": tedSeen = loopCount; break;
                case "Char": charSeen = loopCount; break;
                case "Mike": mikeSeen = loopCount; break;
                case "Matt": mattSeen = loopCount; break;
            }
            ++loopCount;
        }

        assertEquals(4, loopCount); // We should only have gone through the loop 4 times

        // This is the order they should've been seen in
        assertEquals(0, mikeSeen);
        assertEquals(1, mattSeen);
        assertEquals(2, tedSeen);
        assertEquals(3, charSeen);
    }
    @Test public void fivePlayerRotationStartingFromFive() {
        setupTrick(5).setLeadingPlayer(game.getPlayerFromName("Vincent"));

        int tedSeen = -1, charSeen = -1, mikeSeen = -1, mattSeen = -1, vincentSeen = -1;
        int loopCount = 0;
        for (Player player : trick.turnOrder()) {
            switch (player.getName()) {
                case "Ted": tedSeen = loopCount; break;
                case "Char": charSeen = loopCount; break;
                case "Mike": mikeSeen = loopCount; break;
                case "Matt": mattSeen = loopCount; break;
                case "Vincent": vincentSeen = loopCount; break;
            }
            ++loopCount;
        }

        assertEquals(5, loopCount);

        // This is the order they should've been seen in
        assertEquals(0, vincentSeen);
        assertEquals(1, tedSeen);
        assertEquals(2, charSeen);
        assertEquals(3, mikeSeen);
        assertEquals(4, mattSeen);
    }

    @Test public void noPointsTrickScoresZero() {
        setupTrick(4);

        trick.add(new Play(game.getPlayerFromName("Ted"), Card.EightClubs));
        trick.add(new Play(game.getPlayerFromName("Char"), Card.JackClubs));
        trick.add(new Play(game.getPlayerFromName("Mike"), Card.AceClubs));
        trick.add(new Play(game.getPlayerFromName("Matt"), Card.NineClubs));

        assertEquals(Suit.CLUB, trick.getLedSuit());
        assertEquals(game.getPlayerFromName("Mike"), trick.getWinningPlayer());
        assertEquals(Card.AceClubs, trick.getWinningPlay().card);
        assertEquals(0, trick.getScore());
    }
    @Test public void oneHeartTrickScoresOne() {
        setupTrick(4);

        trick.add(new Play(game.getPlayerFromName("Ted"), Card.EightClubs));
        trick.add(new Play(game.getPlayerFromName("Char"), Card.JackHearts));
        trick.add(new Play(game.getPlayerFromName("Mike"), Card.AceClubs));
        trick.add(new Play(game.getPlayerFromName("Matt"), Card.NineClubs));

        assertEquals(Suit.CLUB, trick.getLedSuit());
        assertEquals(game.getPlayerFromName("Mike"), trick.getWinningPlayer());
        assertEquals(Card.AceClubs, trick.getWinningPlay().card);
        assertEquals(1, trick.getScore());
    }
    @Test public void fourHeartsTrickScoresFour() {
        setupTrick(4);

        trick.add(new Play(game.getPlayerFromName("Ted"), Card.EightHearts));
        trick.add(new Play(game.getPlayerFromName("Char"), Card.JackHearts));
        trick.add(new Play(game.getPlayerFromName("Mike"), Card.AceHearts));
        trick.add(new Play(game.getPlayerFromName("Matt"), Card.NineHearts));

        assertEquals(Suit.HEART, trick.getLedSuit());
        assertEquals(game.getPlayerFromName("Mike"), trick.getWinningPlayer());
        assertEquals(Card.AceHearts, trick.getWinningPlay().card);
        assertEquals(4, trick.getScore());
    }
    @Test public void tedLeadsTheQueenAndScoresThirteen() {
        setupTrick(4);

        trick.add(new Play(game.getPlayerFromName("Ted"), Card.QueenSpades));
        trick.add(new Play(game.getPlayerFromName("Char"), Card.JackClubs));
        trick.add(new Play(game.getPlayerFromName("Mike"), Card.AceClubs));
        trick.add(new Play(game.getPlayerFromName("Matt"), Card.NineClubs));

        assertEquals(Suit.SPADE, trick.getLedSuit());
        assertEquals(game.getPlayerFromName("Ted"), trick.getWinningPlayer());
        assertEquals(Card.QueenSpades, trick.getWinningPlay().card);
        assertEquals(13, trick.getScore());
    }
    @Test public void tedIsHavingABadDay() {
        setupTrick(4);

        trick.add(new Play(game.getPlayerFromName("Ted"), Card.ThreeClubs));
        trick.add(new Play(game.getPlayerFromName("Char"), Card.QueenSpades));
        trick.add(new Play(game.getPlayerFromName("Mike"), Card.AceHearts));
        trick.add(new Play(game.getPlayerFromName("Matt"), Card.NineHearts));

        assertEquals(Suit.CLUB, trick.getLedSuit());
        assertEquals(game.getPlayerFromName("Ted"), trick.getWinningPlayer());
        assertEquals(Card.ThreeClubs, trick.getWinningPlay().card);
        assertEquals(15, trick.getScore());
    }
    @Test public void michaelTakesTheJackOnceMoreDamnHim() {
        setupTrick(4, true);

        trick.add(new Play(game.getPlayerFromName("Ted"), Card.EightClubs));
        trick.add(new Play(game.getPlayerFromName("Char"), Card.JackDiamonds));
        trick.add(new Play(game.getPlayerFromName("Mike"), Card.AceClubs));
        trick.add(new Play(game.getPlayerFromName("Matt"), Card.NineHearts));

        assertEquals(Suit.CLUB, trick.getLedSuit());
        assertEquals(game.getPlayerFromName("Mike"), trick.getWinningPlayer());
        assertEquals(Card.AceClubs, trick.getWinningPlay().card);
        assertEquals(-9, trick.getScore());
    }
    @Test public void tedTakesTheJackButItDoesntCount() {
        setupTrick(4, false);

        trick.add(new Play(game.getPlayerFromName("Ted"), Card.EightClubs));
        trick.add(new Play(game.getPlayerFromName("Char"), Card.JackDiamonds));
        trick.add(new Play(game.getPlayerFromName("Mike"), Card.AceHearts));
        trick.add(new Play(game.getPlayerFromName("Matt"), Card.SevenClubs));

        assertEquals(Suit.CLUB, trick.getLedSuit());
        assertEquals(game.getPlayerFromName("Ted"), trick.getWinningPlayer());
        assertEquals(Card.EightClubs, trick.getWinningPlay().card);
        assertEquals(1, trick.getScore());
    }

    @Test public void fivePlayerAllHeartsAndQueenTrickScoresSeventeen() {
        setupTrick(5);

        trick.add(new Play(game.getPlayerFromName("Char"), Card.QueenSpades));
        trick.add(new Play(game.getPlayerFromName("Ted"), Card.SevenHearts));
        trick.add(new Play(game.getPlayerFromName("Mike"), Card.AceHearts));
        trick.add(new Play(game.getPlayerFromName("Matt"), Card.NineHearts));
        trick.add(new Play(game.getPlayerFromName("Vincent"), Card.EightHearts));

        assertEquals(Suit.SPADE, trick.getLedSuit());
        assertEquals(game.getPlayerFromName("Char"), trick.getWinningPlayer());
        assertEquals(Card.QueenSpades, trick.getWinningPlay().card);
        assertEquals(17, trick.getScore());
    }

    @Test public void tryToPlayACardIDontHave() {
        setupTrick(4);

        Player ted = trick.getRound().getGame().getPlayerFromName("Ted");
        Player mike = trick.getRound().getGame().getPlayerFromName("Mike");

        assertEquals(ted, trick.getLeadingPlayer()); // Ted has 2C, should be required to lead
        assertNotEquals("", trick.legalCardToPlay(new Play(ted, Card.FiveClubs)));
            // Not the starting card, so not legal
        assertNotEquals("", trick.legalCardToPlay(new Play(ted, Card.QueenClubs)));
            // Not in Ted's hand, so not legal
        assertNotEquals("", trick.legalCardToPlay(new Play(mike, Card.TwoClubs)));
            // Not the leading Player AND not in Mike's hand, so not legal
    }
}
