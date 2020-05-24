package com.newardassociates.hearts;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Deck implements Iterable<Card> {
    /*private*/ List<Card> cards = new ArrayList<>();

    /**
     * Create a new 52-card deck, shuffle it
     */
    public Deck() {
        for (Rank r : Rank.class.getEnumConstants()) {
            for (Suit s : Suit.class.getEnumConstants()) {
                cards.add(new Card(r, s));
            }
        }
    }
    public Deck(List<Card> cs) {
        cards.addAll(cs);
    }
    public Deck(Card... orderedListOfCards) {
        this(Lists.newArrayList(orderedListOfCards));
    }

    @Override
    public Iterator<Card> iterator() { return cards.iterator(); }

    public void shuffle() { Collections.shuffle(cards); }

    public int cardCount() { return cards.size(); }

    public Deck remove(Card toBeRemoved) {
        cards.remove(toBeRemoved);
        return this;
    }

    public List<Hand> dealHands(int numberOfHands) {
        if ((cards.size() % numberOfHands) != 0) {
            throw new IllegalArgumentException("Cannot evenly deal " + cards.size() + " to " + numberOfHands + " hands");
        }

        List<Hand> hands = new ArrayList<>(numberOfHands);
        for (int i=0; i<numberOfHands; i++) {
            hands.add(new Hand());
        }

        Iterable<List<Card>> partitions = Iterables.partition(cards, (cards.size() / numberOfHands));
        int i=0;
        for (List<Card> part : partitions) {
            hands.get(i++).add(part);
        }

        return hands;
    }
}
