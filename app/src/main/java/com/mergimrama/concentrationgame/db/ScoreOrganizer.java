package com.mergimrama.concentrationgame.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mergimrama.concentrationgame.db.ScoreDbSchema.ScoreTable;
import com.mergimrama.concentrationgame.model.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScoreOrganizer {
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ScoreOrganizer getInstance(Context context) {
        return new ScoreOrganizer(context);
    }

    private ScoreOrganizer(Context context) {
        mContext = context;
        mDatabase = new DbHelper(mContext)
                .getWritableDatabase();
    }

    public void addScore(Score score) {
        ContentValues contentValues = getContentValues(score);

        mDatabase.insert(ScoreTable.NAME, null, contentValues);
    }

    public void updateScore(Score score) {
        String uuidString = score.getUUID().toString();
        ContentValues values = getContentValues(score);

        mDatabase.update(ScoreTable.NAME, values, ScoreTable.Cols.UUID + " = ?",
                new String[] {
                        uuidString
                });
    }

    private static ContentValues getContentValues(Score score) {
        ContentValues values = new ContentValues();
        values.put(ScoreTable.Cols.UUID, score.getUUID().toString());
        values.put(ScoreTable.Cols.SCORE, score.getScore());
        values.put(ScoreTable.Cols.TIME, score.getTime());
        return values;
    }

    private ScoreCursorWrapper queryScores(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ScoreTable.NAME,
                null, //Columns - selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, // having
                null // orderBy
        );

        return new ScoreCursorWrapper(cursor, mContext);
    }

    public Score getScore(UUID id) {
        ScoreCursorWrapper cursor = queryScores(
                ScoreTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getScore();
        } finally {
            cursor.close();
        }
    }

    public List<Score> getScores() {
        List<Score> scores = new ArrayList<>();

        ScoreCursorWrapper cursorWrapper = queryScores(null, null);

        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                scores.add(cursorWrapper.getScore());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return scores;
    }

    public boolean deleteScore(Score score) {
        mDatabase.delete(ScoreTable.NAME, ScoreTable.Cols.UUID + " = ?", new String[] { score.getUUID().toString()});
        return false;
    }
}
