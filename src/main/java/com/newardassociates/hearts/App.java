package com.newardassociates.hearts;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Hearts v0.1");

        ConsoleGameView cgv = new ConsoleGameView();
        Game g = new Game();
        g.attachView(cgv);

        g.prepare();
        g.dealCards();
        g.playTrick(null);
    }
}
