package com.mergimrama.concentrationgame.retrofit;

import com.mergimrama.concentrationgame.model.GallerySerializer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIEndpoints {
    @GET("?method=flickr.photos.getRecent")
    Call<GallerySerializer> getRecentPhotos(@Query("per_page") int perPage, @Query("page") int page);
}
