package com.newardassociates.hearts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class TestView implements View {
    Game.Options options;
    int choiceCt = 0;
    List<Function<Player, Card>> choiceFns = new ArrayList<>();

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
        int current = choiceCt;
        choiceCt++;
        return choiceFns.get(current).apply(player);
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
        Game game = new Game(options);

        TestView view = new TestView(options,
                (ted) -> ted.getHand().remove(0)
        );

        game.attachView(view);
    }
}
