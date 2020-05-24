package com.newardassociates.hearts;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

/**
 * A Hand is a single Player's collection of cards
 */
public class Hand {
    private List<Card> cards = new ArrayList<>();

    public Hand() {
        // Empty hand; nothing to do
    }
    public Hand(List<Card> cs) {
        this.cards.addAll(cs);
    }
    public Hand(Card... cards) {
        this(Lists.newArrayList(cards));
    }

    @Override
    public String toString() {
        return "Hand{" +
                "cards=" + cards +
                '}';
    }

    public void add(Card card) { cards.add(card); }
    public void add(List<Card> cards) { this.cards.addAll(cards); }

    public int handSize() { return cards.size(); }
}
