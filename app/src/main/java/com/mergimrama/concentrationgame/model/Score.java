package com.mergimrama.concentrationgame.model;

import java.util.Date;
import java.util.UUID;

public class Score {
    private UUID mUUID;
    private int mScore;
    private String mTime;

    public Score() {
        this(UUID.randomUUID());
    }

    public Score(UUID uuid) {
        mUUID = uuid;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }
}
