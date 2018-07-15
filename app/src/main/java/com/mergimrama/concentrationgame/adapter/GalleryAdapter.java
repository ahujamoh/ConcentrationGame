package com.mergimrama.concentrationgame.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mergimrama.concentrationgame.R;
import com.mergimrama.concentrationgame.model.GallerySerializer;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryHolder> {
    private static final String TAG = GalleryAdapter.class.getSimpleName();
    private List<GallerySerializer.Photo> mPhotoList = new ArrayList<>();
    private Context mContext;
    private Callbacks mCallbacks;
    private int mCardNumber = 1;
    private String mFirstClicked, mSecondClicked;
    private List<ImageView> mImageViewList = new ArrayList<>();
    private int score = 0;
    private int gallerySize;
    private boolean isBlocked;

    public GalleryAdapter(Context context) {
        mContext = context;
        if (context instanceof Callbacks) {
            mCallbacks = (Callbacks) context;
        } else {
            Log.d(TAG, "Im here");
        }
    }

    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.gallery_item, parent, false);

        return new GalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, int position) {
        GallerySerializer.Photo photo = mPhotoList.get(position);
        holder.bindItem(photo);
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    public void setPhotoList(List<GallerySerializer.Photo> photoList) {
        mPhotoList = photoList;
        notifyDataSetChanged();
        gallerySize = mPhotoList.size() / 2;
    }

    class GalleryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private GallerySerializer.Photo mPhoto;
        private ImageView mImageView;
        private View mProgressBar;

        GalleryHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mProgressBar = itemView.findViewById(R.id.progressBar);
            mImageView.setOnClickListener(this);
            mCallbacks.onStartGame(true);
        }

        void bindItem(GallerySerializer.Photo photo) {
            mPhoto = photo;
        }

        @Override
        public void onClick(View v) {
            if (!isBlocked) {
                animateImageView(mImageView);
                calculateClicks();
            }
        }

        private void animateImageView(final ImageView imageView) {
            final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 0f);
            final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imageView, "scaleX", 0f, 1f);
            oa1.setInterpolator(new DecelerateInterpolator());
            oa2.setInterpolator(new AccelerateDecelerateInterpolator());
            oa1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (!imageView.isSelected()) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        Glide.with(mContext)
                                .load(mPhoto.getUrl())
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        mProgressBar.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        mProgressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(imageView);
                        imageView.setSelected(true);
                    } else {
                        imageView.setImageResource(R.drawable.square);
                        imageView.setSelected(false);
                    }
                    oa2.start();
                }
            });
            oa1.start();
        }

        private void calculateClicks() {
            if (mCardNumber == 1) {
                mFirstClicked = mPhoto.getId();
                mCardNumber = 2;
                mImageView.setEnabled(false);
                mImageViewList.add(mImageView);
            } else if (mCardNumber == 2) {
                mSecondClicked = mPhoto.getId();
                mImageView.setEnabled(false);
                isBlocked = true;
                mImageViewList.add(mImageView);
                if (mFirstClicked.equals(mSecondClicked)) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (ImageView i :
                                    mImageViewList) {
                                i.setVisibility(View.INVISIBLE);
                            }
                            score += 15;
                            isBlocked = false;
                            mImageViewList.clear();
                            mCallbacks.onScoreUpdated(score);
                            gallerySize--;
                            Toast.makeText(mContext, R.string.correct, Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);
                    mCardNumber = 1;
                    checkEnd();
                } else {
                    mCardNumber = 1;
                    mImageView.setEnabled(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (ImageView iv :
                                    mImageViewList) {
                                animateImageView(iv);
                                iv.setEnabled(true);
                            }
                            mImageViewList.clear();
                            isBlocked = false;
                            if (score > 5) {
                                score -= 5;
                            }
                            mCallbacks.onScoreUpdated(score);
                        }
                    }, 1000);
                }
            }

            if (mPhotoList.size() == 0) {
                Toast.makeText(mContext, "Finished", Toast.LENGTH_LONG).show();
                mCallbacks.onStartGame(false);
            }
        }

        private void checkEnd() {
            if (gallerySize == 1) {
                mCallbacks.onStartGame(false);
                Toast.makeText(mContext, "Game has finished", Toast.LENGTH_LONG).show();
                mCallbacks.onFinishedGame(score);
            }
        }
    }

    public interface Callbacks {
        void onScoreUpdated(int scorePoints);
        void onStartGame(boolean hasStarted);
        void onFinishedGame(int score);
    }
}
