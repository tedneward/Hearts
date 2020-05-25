package com.newardassociates.hearts;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A Hand is a single Player's collection of cards
 */
public class Hand
        implements Iterable<Card> {
    private List<Card> cards = new ArrayList<>();

    public Hand() {
        // Empty hand; nothing to do. Presumably cards will be added to
        // the Hand later.
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

    // Sort cards by suit-then-rank order
    private void sort() {
        cards.sort(Card.BY_SUIT_THEN_RANK);
    }

    public void add(Card card) { cards.add(card); }
    public void add(List<Card> cards) { this.cards.addAll(cards); }
    public Card examine(int index) {
        checkArgument(index < cards.size());

        return cards.get(index);
    }
    public Card remove(Card card) {
        checkArgument(cards.contains(card));

        cards.remove(card);
        return card;
    }
    public Card remove(int index) {
        checkArgument(index < cards.size());

        Card returned = cards.get(index);
        cards.remove(index);
        return returned;
    }

    public int handSize() { return cards.size(); }

    @Override
    public Iterator<Card> iterator() {
        return this.cards.iterator();
    }
}
