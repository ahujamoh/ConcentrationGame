package com.mergimrama.concentrationgame.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.mergimrama.concentrationgame.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinner = findViewById(R.id.spin);
    }

    public void startGame(View v) {
        String spinValue = mSpinner.getSelectedItem().toString();
        Intent intent = GameActivity.newIntent(this, spinValue);
        startActivity(intent);
    }
}
