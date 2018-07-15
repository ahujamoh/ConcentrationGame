package com.mergimrama.concentrationgame.db;

public class ScoreDbSchema {
    public static final class ScoreTable {
        public static final String NAME = "scores";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String SCORE = "score";
            public static final String DATE = "date";
        }
    }
}