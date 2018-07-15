package com.mergimrama.concentrationgame.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.mergimrama.concentrationgame.R;
import com.mergimrama.concentrationgame.adapter.GalleryAdapter;
import com.mergimrama.concentrationgame.model.GallerySerializer;
import com.mergimrama.concentrationgame.retrofit.APIEndpoints;
import com.mergimrama.concentrationgame.retrofit.RetrofitCaller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameActivity extends AppCompatActivity implements GalleryAdapter.Callbacks {

    private static final String TAG = GameActivity.class.getSimpleName();
    private static final String EXTRA_CATEGORY = "com.mergimrama.time";

    private Call<GallerySerializer> mResponseCall;
    private Call<GallerySerializer> mResponseBodyCall;
    private RecyclerView mRecyclerView;
    private TextView mTextView;
    private Chronometer mChronometer;
    private GalleryAdapter mGalleryAdapter;
    private View mProgressView;
    private ConstraintLayout mConstraintLayout;

    public static Intent newIntent(Context packageContext, String category) {
        Intent intent = new Intent(packageContext, GameActivity.class);
        intent.putExtra(EXTRA_CATEGORY, category);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        String category = getIntent().getStringExtra(EXTRA_CATEGORY);

        mRecyclerView = findViewById(R.id.recyclerView);
        mTextView = findViewById(R.id.score_text_view);
        mChronometer = findViewById(R.id.chronometer);
        mProgressView = findViewById(R.id.progressView);
        mConstraintLayout = findViewById(R.id.mainConstraintLl);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        mGalleryAdapter = new GalleryAdapter(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);

//        getRecentPhotos();
        if (category != null) {
            getSearchedPhoto(category);
        } else {
            getRecentPhotos();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mResponseCall != null && mResponseCall.isExecuted()) {
            mResponseCall.cancel();
        }
        if (mResponseBodyCall != null && mResponseBodyCall.isExecuted()) {
            mResponseBodyCall.cancel();
        }
    }

    public void getRecentPhotos() {
        showProgress(true);
        mResponseCall = RetrofitCaller.call(APIEndpoints.class).getRecentPhotos(6, 1);
        mResponseCall.enqueue(new Callback<GallerySerializer>() {
            @Override
            public void onResponse(@NonNull Call<GallerySerializer> call, @NonNull Response<GallerySerializer> response) {
                if (response.isSuccessful()) {
                    GallerySerializer gallerySerializer = response.body();
                    if (gallerySerializer != null) {
                        GallerySerializer.Photos photos = gallerySerializer.getPhotos();
                        List<GallerySerializer.Photo> photoList = photos.getPhotos();

                        List<GallerySerializer.Photo> list1 = new ArrayList<>(photoList);
                        Collections.shuffle(list1);
                        List<GallerySerializer.Photo> allLists = new ArrayList<>(list1);
                        Collections.shuffle(photoList);
                        allLists.addAll(photoList);
                        Collections.shuffle(allLists);
                        mGalleryAdapter.setPhotoList(allLists);
                        mRecyclerView.setAdapter(mGalleryAdapter);
                        Log.d(TAG, "successful " + photoList.size());
                        showProgress(false);
                    }
                } else {
                    Log.d(TAG, "not successful");
                    showProgress(false);
                }
            }

            @Override

            public void onFailure(@NonNull Call<GallerySerializer> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
                showProgress(false);
            }
        });
    }

    private void getSearchedPhoto(String text) {
        showProgress(true);
        mResponseBodyCall = RetrofitCaller.call(APIEndpoints.class).getSearchedPhotos(text, 6, 1);
        mResponseBodyCall.enqueue(new Callback<GallerySerializer>() {
            @Override
            public void onResponse(@NonNull Call<GallerySerializer> call, @NonNull Response<GallerySerializer> response) {
                if (response.isSuccessful()) {
                    GallerySerializer gallerySerializer = response.body();
                    if (gallerySerializer != null) {
                        GallerySerializer.Photos photos = gallerySerializer.getPhotos();
                        List<GallerySerializer.Photo> photoList = photos.getPhotos();

                        List<GallerySerializer.Photo> list1 = new ArrayList<>(photoList);
                        Collections.shuffle(list1);
                        List<GallerySerializer.Photo> allLists = new ArrayList<>(list1);
                        Collections.shuffle(photoList);
                        allLists.addAll(photoList);
                        Collections.shuffle(allLists);
                        mGalleryAdapter.setPhotoList(allLists);
                        mRecyclerView.setAdapter(mGalleryAdapter);
                        Log.d(TAG, "successful " + photoList.size());
                        showProgress(false);
                    }
                } else {
                    Log.d(TAG, "not successful");
                    showProgress(false);
                }
            }

            @Override

            public void onFailure(@NonNull Call<GallerySerializer> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
                showProgress(false);
            }
        });
    }

    @Override
    public void onScoreUpdated(int scorePoints) {
        mTextView.setText("Score: " + scorePoints);
    }

    @Override
    public void onStartGame(boolean hasStarted) {
        if (hasStarted) {
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
        } else {
            mChronometer.stop();
        }
    }

    @Override
    public void onFinishedGame(int score) {
        Intent i = ScoreActivity.newIntent(this, score, getChronometerTime());
        startActivity(i);
    }

    private String getChronometerTime() {
        long elapsedMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();
        String time = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(elapsedMillis),
                TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedMillis))
        );
        return time;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getApplicationContext().getResources().getInteger(android.R.integer.config_shortAnimTime);

        mConstraintLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        mConstraintLayout.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mConstraintLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}