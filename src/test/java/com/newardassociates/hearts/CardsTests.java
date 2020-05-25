package com.newardassociates.hearts;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardsTests {
    @Test
    void rankToStringYieldsRightString() {
        Rank r = Rank.ACE;
        assertEquals("A", r.toString());
    }

    @Test
    void suitToStringYieldsRightString() {
        Suit s = Suit.HEART;
        assertEquals("H", s.toString());
    }

    @Test
    void twoOfHeartsYieldsRightString() {
        Card c = new Card(Rank.TWO, Suit.HEART);
        assertEquals("2H", c.toString());

        c = Card.TwoHearts;
        assertEquals("2H", c.toString());
    }

    @Test
    void cardsAreEqual() {
        assertEquals(Card.TwoHearts, new Card(Rank.TWO, Suit.HEART));
        assertEquals(Card.TwoClubs, new Card(Rank.TWO, Suit.CLUB));
        assertEquals(Card.JackDiamonds, new Card(Rank.JACK, Suit.DIAMOND));
        assertEquals(Card.QueenSpades, new Card(Rank.QUEEN, Suit.SPADE));
    }

    @Test
    void stringYieldsTwoOfHearts() {
        assertEquals(Card.TwoHearts, Card.fromString("2H"));
    }

    @Test
    void handComparator() {
        List<Card> cards = new ArrayList<>();
        cards.add(Card.AceClubs);
        cards.add(Card.TwoClubs);
        cards.add(Card.ThreeClubs);
        cards.add(Card.TwoHearts);
        cards.add(Card.QueenHearts);
        cards.add(Card.AceSpades);
        cards.add(Card.FiveHearts);
        cards.add(Card.EightDiamonds);
        System.out.println(cards);

        cards.sort(Card.BY_SUIT_THEN_RANK);
        System.out.println(cards);

        assertEquals(Card.TwoClubs, cards.get(0));
        assertEquals(Card.ThreeClubs, cards.get(1));
        assertEquals(Card.AceSpades, cards.get(7));
    }
}


