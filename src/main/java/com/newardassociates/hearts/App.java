package com.newardassociates.hearts;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final Logger logger = Logger.getLogger("App");

    private static final Option[] optionsArray = {
            //new Option("c", "gui", false, "Run in GUI mode"),
            new Option("d", "debug", false, "Display debugging information/log"),
            new Option("verbose", false, "Display a LOT of debugging information"),
            new Option("v", "version", false, "Display the version and quit"),
            new Option("u", "usage", false, "Dislay the available options")
    };
    private static final Options options = new Options();

    public static void main(String... args) throws IOException {
        logger.entering("App", "main", args);

        for (Option o : optionsArray)
            options.addOption(o);

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, args);
        }
        catch (ParseException parseEx) {
            System.out.println("The command-line you entered didn't parse correctly; please try again.");
            usage();
            return;
        }

        if (commandLine.hasOption("debug")) {
            Logger.getGlobal().addHandler(new FileHandler("hearts.debug.log"));
            logger.info("Starting debug log...");
        }
        if (commandLine.hasOption("verbose")) {
            Logger.getGlobal().setLevel(Level.FINEST);
        }
        if (commandLine.hasOption("usage")) {
            usage();
            return;
        }

        logger.info("Exiting main()");
    }

    public static void usage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java " + App.class.getCanonicalName().toString(), options);
    }
}
