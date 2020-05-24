package com.newardassociates.hearts;

public enum Rank {
    ACE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING;

    @Override public String toString() {
        switch (this) {
            case ACE: return "A";
            case TWO: return "2";
            case THREE: return "3";
            case FOUR: return "4";
            case FIVE: return "5";
            case SIX: return "6";
            case SEVEN: return "7";
            case EIGHT: return "8";
            case NINE: return "9";
            case TEN: return "T";
            case JACK: return "J";
            case QUEEN: return "Q";
            case KING: return "K";
        }
        throw new RuntimeException("Should never reach this code");
    }
}
