package com.mergimrama.concentrationgame.retrofit;

import com.mergimrama.concentrationgame.model.GallerySerializer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIEndpoints {
    @GET("?method=flickr.photos.getRecent")
    Call<GallerySerializer> getRecentPhotos(@Query("per_page") int perPage,
                                            @Query("page") int page);
    @GET("?method=flickr.photos.search")
    Call<GallerySerializer> getSearchedPhotos(@Query("text") String query,
                                         @Query("per_page") int perPage,
                                         @Query("page") int page);
}
