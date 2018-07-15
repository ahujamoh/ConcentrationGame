package com.mergimrama.concentrationgame.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mergimrama.concentrationgame.R;
import com.mergimrama.concentrationgame.model.Score;

import java.util.ArrayList;
import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreHolder> {

    private Context mContext;
    private List<Score> mScoreList = new ArrayList<>();

    public ScoreAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ScoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.score_item, parent, false);

        return new ScoreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreHolder holder, int position) {
        Score score = mScoreList.get(position);
        holder.bindScore(score);
    }

    @Override
    public int getItemCount() {
        return mScoreList.size();
    }

    public void setScoreList(List<Score> scoreList) {
        mScoreList = scoreList;
        notifyDataSetChanged();
    }

    class ScoreHolder extends RecyclerView.ViewHolder {
        private TextView mRowTextView;
        private TextView mScoreTextView;
        private TextView mTimeTextView;
        private Score mScore;

        public ScoreHolder(View itemView) {
            super(itemView);

            mRowTextView = itemView.findViewById(R.id.row_number_text_view);
            mScoreTextView = itemView.findViewById(R.id.score_value_text_view);
            mTimeTextView = itemView.findViewById(R.id.time_text_view);
        }

        void bindScore(Score score) {
            mScore = score;

            mRowTextView.setText(String.format("%d. ", getAdapterPosition() + 1));
            mScoreTextView.setText(String.format("%d", mScore.getScore()));
            mTimeTextView.setText(mScore.getTime());
        }
    }
}
