package com.newardassociates.hearts;

import com.google.common.collect.Iterables;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.google.common.collect.Iterables.*;

public class DeckTests {
    @Test
    public void newDeckContains52Cards() {
        Deck deck = new Deck();
        assertEquals(52, deck.cardCount());

        // Make sure there's four suits, 13 cards each
        for (Suit s : Suit.class.getEnumConstants()) {
            assertEquals(13, size(filter(deck, (card) -> card.suit == s)));
        }

        // Make sure there's 13 ranks, 4 cards each
        for (Rank r : Rank.class.getEnumConstants()) {
            assertEquals(4, size(filter(deck, (card) -> card.rank == r)));
        }
    }

    @Test
    public void threePlayerDeckOnlyHas51Cards() {
        Deck deck = new Deck().remove(Card.TwoClubs);
        assertEquals(51, deck.cardCount());

        boolean found = false;
        for (Card c : deck) {
            if (c.equals(Card.TwoClubs)) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Test
    public void fivePlayerDeckOnlyHas50Cards() {
        Deck deck = new Deck().remove(Card.TwoClubs).remove(Card.TwoHearts);
        assertEquals(50, deck.cardCount());
    }

    @Test
    public void dealFourHands() {
        Deck deck = new Deck();
        List<Hand> hands = deck.dealHands(4);

        for (int i=0; i<4; i++) {
            assertEquals(13, hands.get(i).size());
        }
    }
    @Test
    public void dealThreeHands() {
        Deck deck = new Deck().remove(Card.TwoClubs);
        List<Hand> hands = deck.dealHands(3);

        for (int i=0; i<3; i++) {
            assertEquals(17, hands.get(i).size());
        }
    }
    @Test
    public void dealFiveHands() {
        Deck deck = new Deck().remove(Card.TwoClubs).remove(Card.TwoDiamonds);
        List<Hand> hands = deck.dealHands(5);

        for (int i=0; i<5; i++) {
            assertEquals(10, hands.get(i).size());
        }
    }
}
