package com.newardassociates.hearts;

import java.util.Objects;

public class Card {
    public Rank rank;
    public Suit suit;

    public Card(Rank r, Suit s) { rank = r; suit = s; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return rank == card.rank &&
                suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }

    @Override
    public String toString() {
        return rank.toString() + suit.toString();
    }

    public static Card fromString(String representation) {
        if (representation.length() == 2) {
            char rankCh = representation.charAt(0);
            char suitCh = representation.charAt(1);

            Rank rank;
            Suit suit;
            switch (rankCh) {
                case 'A': rank = Rank.ACE; break;
                case '2': rank = Rank.TWO; break;
                case '3': rank = Rank.THREE; break;
                case '4': rank = Rank.FOUR; break;
                case '5': rank = Rank.FIVE; break;
                case '6': rank = Rank.SIX; break;
                case '7': rank = Rank.SEVEN; break;
                case '8': rank = Rank.EIGHT; break;
                case '9': rank = Rank.NINE; break;
                case 'T': rank = Rank.TEN; break;
                case 'J': rank = Rank.JACK; break;
                case 'Q': rank = Rank.QUEEN; break;
                case 'K': rank = Rank.KING; break;
                default:
                    throw new RuntimeException("Unrecognized rank:" + rankCh);
            }

            switch (suitCh) {
                case 'C': suit = Suit.CLUB; break;
                case 'D': suit = Suit.DIAMOND; break;
                case 'H': suit = Suit.HEART; break;
                case 'S': suit = Suit.SPADE; break;
                default:
                    throw new RuntimeException("Unrecognized suit:" + suitCh);
            }

            return new Card(rank, suit);
        }
        else {
            throw new RuntimeException("Invalid Card representation: " + representation);
        }
    }

    public static final Card TwoClubs = new Card(Rank.TWO, Suit.CLUB);
    public static final Card ThreeClubs = new Card(Rank.THREE, Suit.CLUB);
    public static final Card FourClubs = new Card(Rank.FOUR, Suit.CLUB);
    public static final Card FiveClubs = new Card(Rank.FIVE, Suit.CLUB);
    public static final Card SixClubs = new Card(Rank.SIX, Suit.CLUB);
    public static final Card SevenClubs = new Card(Rank.SEVEN, Suit.CLUB);
    public static final Card EightClubs = new Card(Rank.EIGHT, Suit.CLUB);
    public static final Card NineClubs = new Card(Rank.NINE, Suit.CLUB);
    public static final Card TenClubs = new Card(Rank.TEN, Suit.CLUB);
    public static final Card JackClubs = new Card(Rank.JACK, Suit.CLUB);
    public static final Card QueenClubs = new Card(Rank.QUEEN, Suit.CLUB);
    public static final Card KingClubs = new Card(Rank.KING, Suit.CLUB);

    public static final Card TwoDiamonds = new Card(Rank.TWO, Suit.DIAMOND);
    public static final Card ThreeDiamonds = new Card(Rank.THREE, Suit.DIAMOND);
    public static final Card FourDiamonds = new Card(Rank.FOUR, Suit.DIAMOND);
    public static final Card FiveDiamonds = new Card(Rank.FIVE, Suit.DIAMOND);
    public static final Card SixDiamonds = new Card(Rank.SIX, Suit.DIAMOND);
    public static final Card SevenDiamonds = new Card(Rank.SEVEN, Suit.DIAMOND);
    public static final Card EightDiamonds = new Card(Rank.EIGHT, Suit.DIAMOND);
    public static final Card NineDiamonds = new Card(Rank.NINE, Suit.DIAMOND);
    public static final Card TenDiamonds = new Card(Rank.TEN, Suit.DIAMOND);
    public static final Card JackDiamonds = new Card(Rank.JACK, Suit.DIAMOND);
    public static final Card QueenDiamonds = new Card(Rank.QUEEN, Suit.DIAMOND);
    public static final Card KingDiamonds = new Card(Rank.KING, Suit.DIAMOND);

    public static final Card TwoHearts = new Card(Rank.TWO, Suit.HEART);
    public static final Card ThreeHearts = new Card(Rank.THREE, Suit.HEART);
    public static final Card FourHearts = new Card(Rank.FOUR, Suit.HEART);
    public static final Card FiveHearts = new Card(Rank.FIVE, Suit.HEART);
    public static final Card SixHearts = new Card(Rank.SIX, Suit.HEART);
    public static final Card SevenHearts = new Card(Rank.SEVEN, Suit.HEART);
    public static final Card EightHearts = new Card(Rank.EIGHT, Suit.HEART);
    public static final Card NineHearts = new Card(Rank.NINE, Suit.HEART);
    public static final Card TenHearts = new Card(Rank.TEN, Suit.HEART);
    public static final Card JackHearts = new Card(Rank.JACK, Suit.HEART);
    public static final Card QueenHearts = new Card(Rank.QUEEN, Suit.HEART);
    public static final Card KingHearts = new Card(Rank.KING, Suit.HEART);

    public static final Card TwoSpades = new Card(Rank.TWO, Suit.SPADE);
    public static final Card ThreeSpades = new Card(Rank.THREE, Suit.SPADE);
    public static final Card FourSpades = new Card(Rank.FOUR, Suit.SPADE);
    public static final Card FiveSpades = new Card(Rank.FIVE, Suit.SPADE);
    public static final Card SixSpades = new Card(Rank.SIX, Suit.SPADE);
    public static final Card SevenSpades = new Card(Rank.SEVEN, Suit.SPADE);
    public static final Card EightSpades = new Card(Rank.EIGHT, Suit.SPADE);
    public static final Card NineSpades = new Card(Rank.NINE, Suit.SPADE);
    public static final Card TenSpades = new Card(Rank.TEN, Suit.SPADE);
    public static final Card JackSpades = new Card(Rank.JACK, Suit.SPADE);
    public static final Card QueenSpades = new Card(Rank.QUEEN, Suit.SPADE);
    public static final Card KingSpades = new Card(Rank.KING, Suit.SPADE);
}
