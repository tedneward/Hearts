package com.newardassociates.hearts;

public interface View {
    Game.Options getOptions();

    void display(String message);
    void display(Player player);

    Card chooseCard(Player player);
}
