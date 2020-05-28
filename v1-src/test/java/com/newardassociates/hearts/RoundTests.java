package com.newardassociates.hearts;

import com.newardassociates.hearts.util.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoundTests {

    @Test
    public void heartsBrokenReturnsTrueWhenAHeartIsInTheTrickList() {
        Game g = new Game();
        Game.Round round = g.new Round();

        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        Player p3 = new Player("p3");
        Player p4 = new Player("p4");

        Game.Trick trick = g.new Trick();
        trick.plays.add(new Pair<>(p1, Card.NineClubs));
        trick.plays.add(new Pair<>(p2, Card.ThreeHearts));
        trick.plays.add(new Pair<>(p3, Card.SevenClubs));
        trick.plays.add(new Pair<>(p4, Card.ThreeClubs));
        round.tricks.add(trick);

        trick = g.new Trick();
        trick.plays.add(new Pair<>(p1, Card.FiveClubs));
        trick.plays.add(new Pair<>(p2, Card.JackSpades));
        trick.plays.add(new Pair<>(p3, Card.EightClubs));
        trick.plays.add(new Pair<>(p4, Card.JackClubs));
        round.tricks.add(trick);

        assertTrue(round.heartsBroken());
    }

    @Test
    public void heartsBrokenReturnsFalseWhenNoHeartsInTricks() {
        Game g = new Game();
        Game.Round round = g.new Round();

        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        Player p3 = new Player("p3");
        Player p4 = new Player("p4");

        Game.Trick trick = g.new Trick();
        trick.plays.add(new Pair<>(p1, Card.NineClubs));
        trick.plays.add(new Pair<>(p2, Card.FiveDiamonds));
        trick.plays.add(new Pair<>(p3, Card.SevenClubs));
        trick.plays.add(new Pair<>(p4, Card.ThreeClubs));
        round.tricks.add(trick);

        trick = g.new Trick();
        trick.plays.add(new Pair<>(p1, Card.FiveClubs));
        trick.plays.add(new Pair<>(p2, Card.JackSpades));
        trick.plays.add(new Pair<>(p3, Card.EightClubs));
        trick.plays.add(new Pair<>(p4, Card.JackClubs));
        round.tricks.add(trick);

        assertFalse(round.heartsBroken());
    }


}
