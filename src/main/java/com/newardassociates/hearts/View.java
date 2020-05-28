package com.newardassociates.hearts;

public interface View {
    void attachGame(Game game);

    Game.Options getOptions();

    void display(String message);
    void display(Player player);

    Card chooseCard(Player player);
}
