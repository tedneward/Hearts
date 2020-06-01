package com.newardassociates.hearts;

import java.util.Comparator;
import java.util.Objects;

/**
 * A Player/Card pair as part of a Trick.
 */
public class Play {
    public Player player;
    public Card card;

    public Play(Player player, Card card) { this.player = player; this.card = card; }

    public static int compareRank(Play p1, Play p2) { return p1.card.rank.ordinal() - p2.card.rank.ordinal(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Play play = (Play) o;
        return player.equals(play.player) &&
                card.equals(play.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, card);
    }

    @Override
    public String toString() {
        return "Play{" +
                "player=" + player +
                ", card=" + card +
                '}';
    }
}
