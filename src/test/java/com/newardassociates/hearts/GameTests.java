package com.newardassociates.hearts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class TestView implements View {
    Game.Options options;
    int choiceCt = 0;
    List<Function<Player, Card>> choiceFns = new ArrayList<>();

    @SafeVarargs
    TestView(Game.Options options, Function<Player, Card>... choices) {
        this.options = options;
        this.choiceFns.addAll(Arrays.asList(choices));
    }

    @Override public Game.Options getOptions() { return options; }

    @Override public void display(String message) { }

    @Override public void display(Player player) { }

    @Override
    public Card chooseCard(Player player) {
        // If we roll off the edge, then so be it--it's a bug in the tests, so throw the exception
        Card card = choiceFns.get(choiceCt++).apply(player);
        //System.out.println(player.getName() + " plays " + card);
        return card;
    }
}

public class GameTests {
    private static Game.Options options;

    @BeforeEach
    public void prepGameOptions() {
        options = new Game.Options();
        options.playerNames.clear();
        options.playerNames.add("Ted");
        options.playerNames.add("Char");
        options.playerNames.add("Mike");
        options.playerNames.add("Matt");
    }

    @Test
    public void attachATestView() {
        Game game = new Game();

        TestView view = new TestView(options,
                (ted) -> ted.getHand().remove(0)
        );

        game.attachView(view);
    }

    @Test
    public void fourPlayersShouldStartWithTwoOfClubs() {
        Game.Options options = new Game.Options();
        options.numberOfPlayers = 4;
        assertEquals(Card.TwoClubs, options.getStartingCard());
    }

    @Test
    public void playOneRound() {
        Game game = new Game();
        TestView view = new TestView(options,
                (ted) -> ted.getHand().examine(0),
                (charl) -> charl.getHand().examine(0),
                (mike) -> mike.getHand().examine(0),
                (matt) -> matt.getHand().examine(0)
                );
        game.attachView(view);
        game.prepare();

        Map<String, List<Card>> handMap = Map.of(
                "Ted", Collections.singletonList(Card.TwoClubs),
                "Char", Collections.singletonList(Card.ThreeClubs),
                "Mike", Collections.singletonList(Card.FourClubs),
                "Matt", Collections.singletonList(Card.FiveClubs)
        );

        for (Player player : game.getPlayers()) {
            Hand hand = new Hand();
            hand.add(handMap.get(player.getName()));
            player.setHand(hand);
        }

        Game.Trick firstTrick = game.playTrick(null);
        assertEquals(game.getPlayerForName("Matt"), firstTrick.getWinningPlayer());
        assertEquals(0, firstTrick.getScore());
    }
}
