package com.newardassociates.hearts;

public interface View {
    Game.Options getOptions();

    void display(String message);
    void display(Player player);
    void displayScores(Game game);

    Card chooseCard(Player player);
}
