package com.newardassociates.hearts;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.*;

public class Deck
        implements Iterable<Card> {
    private final List<Card> cards = new ArrayList<>();

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
        checkArgument((cards.size() % numberOfHands) == 0,
                "Cannot evenly deal %s to %s hands", cards.size(), numberOfHands);

        List<Hand> hands = new ArrayList<>(numberOfHands);
        for (List<Card> part : Iterables.partition(cards, (cards.size() / numberOfHands))) {
            hands.add(new Hand(part));
        }
        return hands;
    }
}
