package com.mergimrama.concentrationgame.db;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import com.mergimrama.concentrationgame.db.ScoreDbSchema.ScoreTable;
import com.mergimrama.concentrationgame.model.Score;

import java.util.Date;
import java.util.UUID;

public class ScoreCursorWrapper extends CursorWrapper {
    private Context mContext;

    public ScoreCursorWrapper(Cursor cursor, Context context) {
        super(cursor);
        mContext = context;
    }

    public Score getScore() {
        String uuidString = getString(getColumnIndex(ScoreTable.Cols.UUID));
        int scoreValue = getInt(getColumnIndex(ScoreTable.Cols.SCORE));
        String time = getString(getColumnIndex(ScoreTable.Cols.TIME));

        Score score = new Score(UUID.fromString(uuidString));
        score.setScore(scoreValue);
        score.setTime(time);

        return score;
    }
}
