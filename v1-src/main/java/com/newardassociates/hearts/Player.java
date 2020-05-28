package com.newardassociates.hearts;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private Hand hand;

    public int score = 0;
    public List<Game.Trick> tricks = new ArrayList<>();

    public Player(String n) {
        name = n;
    }

    public String getName() { return name; }

    /**
     * @return A reference to the Player's Hand
     */
    public Hand getHand() { return hand; }

    /**
     * @param h Becomes the reference stored in the Player for their Hand
     */
    public void setHand(Hand h) { hand = h; }

    public void add(Game.Trick trick) {
        tricks.add(trick);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", hand=" + hand +
                '}';
    }
}
