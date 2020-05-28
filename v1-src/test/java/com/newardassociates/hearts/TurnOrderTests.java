package com.newardassociates.hearts;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TurnOrderTests {
    static List<Player> fourPlayers;
    static List<Player> fivePlayers;

    @BeforeAll
    public static void fourPlayers() {
        fourPlayers = new ArrayList<>(4);
        fourPlayers.add(new Player("Ted"));
        fourPlayers.add(new Player("Charlotte"));
        fourPlayers.add(new Player("Mike"));
        fourPlayers.add(new Player("Matt"));
    }

    @BeforeAll
    public static void fivePlayers() {
        fivePlayers = new ArrayList<>(5);
        fivePlayers.add(new Player("Ted"));
        fivePlayers.add(new Player("Charlotte"));
        fivePlayers.add(new Player("Mike"));
        fivePlayers.add(new Player("Matt"));
        fivePlayers.add(new Player("Vincent"));
    }

    @Test
    public void startFromIndexZeroAndGetsAllPlayers() {
        int tedSeen = -1, charSeen = -1, mikeSeen = -1, mattSeen = -1;
        Player startingPlayer = fourPlayers.get(0);
        int loopCount = 0;
        for (Player player : Game.turnOrder(fourPlayers, startingPlayer)) {
            switch (player.getName()) {
                case "Ted":
                    tedSeen = loopCount;
                    break;
                case "Charlotte":
                    charSeen = loopCount;
                    break;
                case "Mike":
                    mikeSeen = loopCount;
                    break;
                case "Matt":
                    mattSeen = loopCount;
                    break;
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

    @Test
    public void startFromIndexTwoAndGetsAllPlayers() {
        int tedSeen = -1, charSeen = -1, mikeSeen = -1, mattSeen = -1;
        Player startingPlayer = fourPlayers.get(2);
        int loopCount = 0;
        for (Player player : Game.turnOrder(fourPlayers, startingPlayer)) {
            switch (player.getName()) {
                case "Ted":
                    tedSeen = loopCount;
                    break;
                case "Charlotte":
                    charSeen = loopCount;
                    break;
                case "Mike":
                    mikeSeen = loopCount;
                    break;
                case "Matt":
                    mattSeen = loopCount;
                    break;
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