package com.newardassociates.hearts;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A Hand is a single Player's collection of cards
 */
public class Hand
        implements Iterable<Card> {
    private final List<Card> cards = new ArrayList<>();

    public Hand() {
        // Empty hand; nothing to do. Presumably cards will be added to
        // the Hand later.
    }
    public Hand(List<Card> cs) {
        this.cards.addAll(cs);
        sort();
    }
    public Hand(Card... cards) {
        this(Lists.newArrayList(cards));
    }

    /**
     * Create a Hand from a String representation (according to the encoding used by Card)
     *
     * @param cards A space-separated list of rank-suit pairs (2C 2S 2H == TwoClubs, TwoSpades, TwoHearts)
     */
    public Hand(String cards) {
        this(Card.collectionFromString(cards));
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
    public boolean contains(Card card) {
        return cards.contains(card);
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

    public int size() { return cards.size(); }

    // Not sure why NotNull isn't available to my compile path; fix this later
    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<Card> iterator() {
        return this.cards.iterator();
    }
}
