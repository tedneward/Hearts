package com.newardassociates.hearts;

public enum Suit {
    HEART,
    DIAMOND,
    CLUB,
    SPADE;

    @Override public String toString() {
        switch (this) {
            case HEART: return "H";
            case DIAMOND: return "D";
            case CLUB: return "C";
            case SPADE: return "S";
        }
        throw new RuntimeException("Impossible to reach this code");
    }
}
