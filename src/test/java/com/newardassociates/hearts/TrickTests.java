package com.newardassociates.hearts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrickTests {

    Game getGame(boolean jack) {
        Game game = new Game();

        Game.Options options = new Game.Options();
        options.playerNames.clear();
        options.playerNames.add("Ted");
        options.playerNames.add("Char");
        options.playerNames.add("Mike");
        options.playerNames.add("Matt");
        options.jackOfDiamondsSubtractsTen = jack;

        TestView view = new TestView(options);
        game.attachView(view);
        game.prepare();

        return game;
    }

    @Test
    public void allClubTrick() {
        Game game = getGame(true); // irrelevant for this test
        Game.Trick trick = game.new Trick();

        trick.play(game.getPlayerForName("Ted"), Card.SixClubs);
        trick.play(game.getPlayerForName("Char"), Card.SevenClubs);
        trick.play(game.getPlayerForName("Mike"), Card.QueenClubs);
        trick.play(game.getPlayerForName("Matt"), Card.ThreeClubs);

        assertEquals("Ted", trick.getLeadingPlayer().getName());
        assertEquals(Suit.CLUB, trick.getLeadingSuit());
        assertEquals("Mike", trick.getWinningPlayer().getName());
        assertEquals(Card.QueenClubs, trick.getWinningCard());
        assertEquals(0, trick.getScore());
    }
    @Test
    public void mixedClubWithDiamondsTrick() {
        Game game = getGame(true); // irrelevant for this test
        Game.Trick trick = game.new Trick();

        trick.play(game.getPlayerForName("Ted"), Card.SixClubs);
        trick.play(game.getPlayerForName("Char"), Card.SevenClubs);
        trick.play(game.getPlayerForName("Mike"), Card.QueenDiamonds);
        trick.play(game.getPlayerForName("Matt"), Card.ThreeClubs);

        assertEquals("Char", trick.getWinningPlayer().getName());
        assertEquals(Card.SevenClubs, trick.getWinningCard());
        assertEquals(0, trick.getScore());
    }
    @Test
    public void justOneDiamondLedButTakesTheTrickAnyway() {
        Game game = getGame(true); // irrelevant for this test
        Game.Trick trick = game.new Trick();

        trick.play(game.getPlayerForName("Ted"), Card.SixDiamonds);
        trick.play(game.getPlayerForName("Char"), Card.SevenClubs);
        trick.play(game.getPlayerForName("Mike"), Card.QueenClubs);
        trick.play(game.getPlayerForName("Matt"), Card.ThreeClubs);

        assertEquals("Ted", trick.getWinningPlayer().getName());
        assertEquals(Card.SixDiamonds, trick.getWinningCard());
        assertEquals(0, trick.getScore());
    }
}
