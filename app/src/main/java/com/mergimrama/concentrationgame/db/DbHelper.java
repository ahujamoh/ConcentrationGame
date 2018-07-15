package com.mergimrama.concentrationgame.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mergimrama.concentrationgame.db.ScoreDbSchema.ScoreTable;

public class DbHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "scoreBase.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + ScoreTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ScoreTable.Cols.UUID + ", " +
                ScoreTable.Cols.SCORE + ", " +
                ScoreTable.Cols.DATE + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}