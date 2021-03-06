package com.newardassociates.hearts;

import java.io.PrintWriter;
import java.util.Map;

public class ConsoleGameView implements View {
    public ConsoleGameView() { }
    // Future enhancement: take stdin/stdout as constructor parameters?

    private int prompt(int def, String message, Object... opts) {
        String input = System.console().readLine(message + "(ENTER for " + def + "): ", opts);
        if (input.equals("")) return def;
        return Integer.parseInt(input);
    }
    private boolean prompt(boolean def, String message, Object... opts) {
        String input = System.console().readLine(message + "(ENTER for " + def + "): ", opts);
        if (input.equals(""))
            return def;
        if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y"))
            return true;
        return Boolean.parseBoolean(input);
    }
    private String prompt(String message, Object... opts) {
        return System.console().readLine(message + ":", opts);
    }

    @Override
    public Game.Options getOptions() {
        Game.Options options = new Game.Options();

        options.numberOfPlayers = prompt(4, "How many players are playing?");

        for (int i=0; i<options.numberOfPlayers; i++) {
            String input = prompt("\tPlease enter the name for Player %s", i);
            options.playerNames.add(input);
        }

        options.winThreshold = prompt(100, "What's the win threshold?");
        options.hittingThresholdExactlyResetsToZero = prompt(false, "Does hitting the win threshold exactly reset the player's score to zero?");
        options.bloodOnFirstRound = prompt(false, "Should we allow points on the first round?");
        options.queenOfSpadesIsAHeart = prompt(true, "Does the Queen of Spades count as a heart?");
        options.shootingAdds26ToOthers = prompt(true, "Does shooting add 26 to others, or subtract 26 from the shooter?");

        display("OK, here's what you selected:");
        display("We have " + options.numberOfPlayers + " players:");
        for (String n : options.playerNames) {
            display("\t" + n);
        }
        // Message for each option
        display("Let's deal cards!");

        return options;
    }

    @Override
    public void display(String message) {
        System.console().writer().println(message);
        System.console().writer().flush();
    }
    @Override
    public void display(Player player) {
        System.console().writer().println(player);
    }

    @Override
    public void displayScores(Game game) {
        System.console().writer().println("Scores:");
        Map<Player, Integer> scores = game.getScores();
        for (Player p : game.getPlayers()) {
            System.console().writer().println("\t" + p.getName() + ": " + scores.get(p));
        }
    }

    @Override
    public Card chooseCard(Player player) {
        System.console().writer().println("Player " + player.getName() + ":");
        int i=0;
        for (Card card : player.getHand()) {
            System.console().writer().print(i + ":" + card + " ");
            i++;
            if ((i % 5) == 0) {
                System.console().writer().println();
            }
        }
        System.console().writer().println();
        System.console().printf("Please choose a card by index: ");
        String card = System.console().readLine();
        return Card.fromString(card);
    }
}
