package com.newardassociates.hearts;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        ConsoleGameView cgv = new ConsoleGameView();
        cgv.display("Welcome to Hearts v0.1");

        Game g = new Game();
        g.attachView(cgv);

        if (g.prepare()) {
            g.dealCards();
            g.playTrick(null);
        }
        else {
            cgv.display("Whoops! Something went wrong");
        }
    }
}
