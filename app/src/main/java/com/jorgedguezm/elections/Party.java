package com.jorgedguezm.elections;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by jorge on 13/11/16.
 */

public class Party {
    private String name;
    private int elects;
    private int votes;
    private Votes theVotes;
    private String color;

    public Party(String n, int e, int v, Votes tV, String c) {
        name = n;
        elects = e;
        votes = v;
        theVotes = tV;
        color = c;
    }

    public String getName() {
        return name;
    }

    public int getElects() {
        return elects;
    }

    public int getVotes() {
        return votes;
    }

    public String getColor() {
        return color;
    }

    public BigDecimal getPercent() {
        return new BigDecimal(((double) votes/theVotes.getCounted()) * 100)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
