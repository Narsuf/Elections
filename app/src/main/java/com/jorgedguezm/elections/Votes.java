package com.jorgedguezm.elections;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by jorge on 13/11/16.
 */

public class Votes implements Parcelable {
    private int counted;
    private int abstentions;
    private int nullVote;
    private int blankVote;
    private double scrutinized;
    private int totalVotes;

    public Votes(int c, int a, int n, int b, double s) {
        counted = c;
        abstentions = a;
        nullVote = n;
        blankVote = b;
        scrutinized = s;
        totalVotes = c + a;
    }

    protected Votes(Parcel in) {
        counted = in.readInt();
        abstentions = in.readInt();
        nullVote = in.readInt();
        blankVote = in.readInt();
        scrutinized = in.readDouble();
        totalVotes = in.readInt();
    }

    public static final Creator<Votes> CREATOR = new Creator<Votes>() {
        @Override
        public Votes createFromParcel(Parcel in) {
            return new Votes(in);
        }

        @Override
        public Votes[] newArray(int size) {
            return new Votes[size];
        }
    };

    public int getCounted() {
        return counted;
    }

    public int getAbstentions() {
        return abstentions;
    }

    public int getNullVote() {
        return nullVote;
    }

    public int getBlankVote() {
        return blankVote;
    }

    public BigDecimal getScrutinized() {
        return new BigDecimal(scrutinized).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPercentageOfParticipation() {
        return new BigDecimal(((double) counted/totalVotes)*100).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPercentageOfAbstentions() {
        return new BigDecimal(((double) abstentions/totalVotes)*100)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPercentageOfNull() {
        return new BigDecimal(((double) nullVote/counted)*100).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPercentageOfBlank() {
        return new BigDecimal(((double) blankVote/counted)*100).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(counted);
        dest.writeInt(abstentions);
        dest.writeInt(nullVote);
        dest.writeInt(blankVote);
        dest.writeDouble(scrutinized);
        dest.writeInt(totalVotes);
    }
}
