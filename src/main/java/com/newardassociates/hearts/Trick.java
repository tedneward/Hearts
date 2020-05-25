package com.newardassociates.hearts;

import java.util.ArrayList;
import java.util.List;

public class Trick {
    public static class Play {
        Player player;
        Card card;
    }

    public List<Play> plays = new ArrayList<>(4);
}
