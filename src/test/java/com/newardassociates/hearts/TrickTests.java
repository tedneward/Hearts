package com.newardassociates.hearts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrickTests {
    Game game;
    Round round;
    Trick trick;

    @BeforeEach
    public void setupGameRoundAndTrick() {
        Game.Options options = new Game.Options();
        options.playerNames.add("Ted");
        options.playerNames.add("Char");
        options.playerNames.add("Mike");
        options.playerNames.add("Matt");
        game = new Game(options);
        round = game.beginRound();
        trick = new Trick(round);
    }
    @AfterEach
    public void cleanupGameRoundAndTrick() {
        trick = null;
        round = null;
        game = null;
    }

    @Test public void turnRotationStartingFromPlayerZero() {
        trick.setLeadingPlayer(game.getPlayerFromName("Ted"));

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
        trick.setLeadingPlayer(game.getPlayerFromName("Mike"));

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
        assertEquals(2, tedSeen);
        assertEquals(3, charSeen);
        assertEquals(0, mikeSeen);
        assertEquals(1, mattSeen);
    }

}
