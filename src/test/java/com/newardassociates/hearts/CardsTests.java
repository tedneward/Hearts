package com.newardassociates.hearts;

import org.junit.jupiter.api.Test;
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
    }
}


