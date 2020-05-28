package com.newardassociates.hearts;

import java.io.IOException;
import java.util.logging.*;
import org.apache.commons.cli.*;

public class App {
    public static Logger logger = Logger.getLogger("com.newardassociates.hearts.App");

    private static final Option[] options = {
        new Option("c","console", false, "Run in console mode"),
        new Option("v", "version", false, "Display the version and quit"),
        new Option("d", "debug", false, "Display debugging information/log"),
        new Option("u", "usage", false, "Dislay the available options")
    };

    private static CommandLine processOptions(String... args) {
        Options opts = new Options();
        for (Option o : options)
            opts.addOption(o);

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(opts, args);
        }
        catch (ParseException parseEx) {
            System.out.println("The command-line you entered didn't parse correctly; please try again.");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "hearts", opts );
        }

        if (commandLine.hasOption("usage")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("hearts", opts);
        }

        if (commandLine.hasOption("debug")) {
            // turn on some diagnostic logging
            try {
                Logger.getGlobal().setLevel(Level.INFO);
                Logger.getGlobal().addHandler(new FileHandler("hearts.debug.log"));
                App.logger.info("Starting debug log");
            }
            catch (IOException ioEx) {
                throw new RuntimeException("Could not open log file", ioEx);
            }
        }

        return commandLine;
    }

    public static void main(String[] args) {
        logger.entering(App.class.getCanonicalName(), "main", args);

        CommandLine commandLine = processOptions(args);
        if (commandLine.hasOption("version")) {
            System.out.println("Hearts v0.1");
            return;
        }

        Game g = new Game();
        View view;
        if (commandLine.hasOption("gui")) {
            // Gui view
            App.logger.severe("Trying to use non-existent GUI view!");
            view = null;
        }
        else if (commandLine.hasOption("console")) {
            App.logger.info("Using console UI");
            view = new ConsoleGameView();
            view.display("Welcome to Hearts v0.1");
            g.attachView(view);
        }
        else {
            App.logger.info("Assuming console UI");
            view = new ConsoleGameView();
            view.display("Welcome to Hearts v0.1");
            g.attachView(view);
        }

        if (! g.prepare()) {
            view.display("Whoops! Something went wrong");
            return;
        }
        else
        {
            App.logger.info("Let's play a game");
            while (! g.gameOver()) {
                g.playRound();
                view.displayScores();
            }
            view.display("Game over! Hope you had fun!");
        }
    }
}
