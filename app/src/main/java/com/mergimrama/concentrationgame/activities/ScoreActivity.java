package com.mergimrama.concentrationgame.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mergimrama.concentrationgame.R;

public class ScoreActivity extends AppCompatActivity {
    private static final String TAG = ScoreActivity.class.getSimpleName();
    private static final String EXTRA_SCORE = "com.mergimrama.score";
    private static final String EXTRA_TIME = "com.mergimrama.time";

    private TextView mScoreTextView;
    private TextView mTimeTextView;

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

        int score = getIntent().getIntExtra(EXTRA_SCORE, -1);
        String time = getIntent().getStringExtra(EXTRA_TIME);

        mScoreTextView.setText(String.format("%d", score));
        mTimeTextView.setText(time);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
