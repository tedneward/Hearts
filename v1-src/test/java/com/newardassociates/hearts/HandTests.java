package com.newardassociates.hearts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class HandTests {
    @Test
    public void constructHandFromString() {
        Hand hand = new Hand("2C 2H 2S 2D");
        assertEquals(4, hand.handSize());
    }
}
