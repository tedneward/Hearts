package com.newardassociates.hearts;

public class Player {
    private final String name;
    private Hand hand;

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

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", hand=" + hand +
                '}';
    }
}
