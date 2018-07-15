package com.mergimrama.concentrationgame.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mergimrama.concentrationgame.R;
import com.mergimrama.concentrationgame.adapter.ScoreAdapter;
import com.mergimrama.concentrationgame.db.ScoreOrganizer;
import com.mergimrama.concentrationgame.model.Score;

import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    private static final String TAG = ScoreActivity.class.getSimpleName();
    private static final String EXTRA_SCORE = "com.mergimrama.score";
    private static final String EXTRA_TIME = "com.mergimrama.time";

    private TextView mScoreTextView;
    private TextView mTimeTextView;
    private RecyclerView mRecyclerView;
    private ScoreAdapter mScoreAdapter;

    public static Intent newIntent(Context packageContext, int score, String time) {
        Intent intent = new Intent(packageContext, ScoreActivity.class);
        intent.putExtra(EXTRA_SCORE, score);
        intent.putExtra(EXTRA_TIME, time);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        mScoreTextView = findViewById(R.id.player_score);
        mTimeTextView = findViewById(R.id.time);
        mRecyclerView = findViewById(R.id.scores_recycler_view);

        mScoreAdapter = new ScoreAdapter(getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        int score = getIntent().getIntExtra(EXTRA_SCORE, -1);
        String time = getIntent().getStringExtra(EXTRA_TIME);

        mScoreTextView.setText(String.format("%d", score));
        mTimeTextView.setText(time);

        writeToDb(score, time);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void writeToDb(int scoreValue, String time) {
        Score score = new Score();
        score.setScore(scoreValue);
        score.setTime(time);
        ScoreOrganizer.getInstance(getApplicationContext()).addScore(score);
    }

    public void getScores(View view) {
        List<Score> scores = ScoreOrganizer.getInstance(getApplicationContext()).getScores();
        if (scores == null)
            return;
        mScoreAdapter.setScoreList(scores);
        mRecyclerView.setAdapter(mScoreAdapter);
    }
}
