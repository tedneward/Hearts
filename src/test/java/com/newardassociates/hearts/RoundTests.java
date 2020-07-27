package com.newardassociates.hearts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoundTests {

    Round setupRound() {
        Game.Options options = new Game.Options();
        options.playerNames.add("Ted");
        options.playerNames.add("Char");
        options.playerNames.add("Mike");
        options.numberOfPlayers = options.playerNames.size();

        Game game = new Game(options);

        return game.beginRound();
    }

    // Test dealing hands
    @Test public void dealHandsToThreePlayers() {
        Round round = setupRound();
        assertEquals(3, round.getGame().getPlayers().size());

        round.setDeck(round.getGame().getDeck(false));
        assertEquals(51, round.getDeck().cardCount());

        round.dealHands();
        for (Player player : round.getGame().getPlayers()) {
            Hand hand = player.getHand();
            System.out.println("Player " + player);
            assertEquals(round.getGame().getHandSize(), hand.size());
        }
    }
    // Let's play a few tricks
    @Test public void firstTrickWithThreePlayers() {
        Round round = setupRound();
        round.setDeck(round.getGame().getDeck(false));
        round.dealHands();

        Player ted = round.getGame().getPlayerFromName("Ted"); // everything low
        Player charl = round.getGame().getPlayerFromName("Char"); // everything mid-range
        Player mike = round.getGame().getPlayerFromName("Mike"); // everything high

        // FIRST TRICK: Ted plays 3C, Char plays 7C, Mike plays JC
        Trick trick = round.beginTrick();
        assertEquals(ted, trick.getLeadingPlayer());

        Play play = new Play(ted, ted.getHand().remove(Card.ThreeClubs));
        assertEquals("", trick.legalCardToPlay(play));
        trick.add(play);

        play = new Play(charl, charl.getHand().remove(Card.SevenClubs));
        assertEquals("", trick.legalCardToPlay(play));
        trick.add(play);

        play = new Play(mike, mike.getHand().remove(Card.JackClubs));
        assertEquals("", trick.legalCardToPlay(play));
        trick.add(play);

        for (Player player : round.getGame().getPlayers()) {
            assertEquals(round.getGame().getHandSize() - 1, player.getHand().size());
        }
        assertFalse(round.endTrick()); // Round should not be over yet
        assertEquals(mike, trick.getWinningPlay().player);
        assertEquals(Card.JackClubs, trick.getWinningPlay().card);

        // SECOND TRICK: Mike plays QC, Ted plays 4C, Char plays 8C
        trick = round.beginTrick();
        assertEquals(mike, trick.getLeadingPlayer());

        play = new Play(mike, mike.getHand().remove(Card.QueenClubs));
        assertEquals("", trick.legalCardToPlay(play));
        trick.add(play);

        play = new Play(ted, ted.getHand().remove(Card.FourClubs));
        assertEquals("", trick.legalCardToPlay(play));
        trick.add(play);

        play = new Play(charl, charl.getHand().remove(Card.EightClubs));
        assertEquals("", trick.legalCardToPlay(play));
        trick.add(play);

        for (Player player : round.getGame().getPlayers()) {
            assertEquals(round.getGame().getHandSize() - 2, player.getHand().size());
        }
        assertFalse(round.endTrick()); // Round should not be over yet
        assertEquals(mike, trick.getWinningPlay().player);
        assertEquals(Card.QueenClubs, trick.getWinningPlay().card);
    }
}
