package com.mergimrama.concentrationgame.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GallerySerializer {
    @SerializedName("photos")
    private Photos mPhotos;

    public Photos getPhotos() {
        return mPhotos;
    }

    public class Photos {
        @SerializedName("page")
        private String mPage;
        @SerializedName("pages")
        private String mPages;
        @SerializedName("perpage")
        private String mPerPage;
        @SerializedName("total")
        private String mTotal;
        @SerializedName("photo")
        private List<Photo> mPhotos;

        public String getPage() {
            return mPage;
        }

        public String getPages() {
            return mPages;
        }

        public String getPerPage() {
            return mPerPage;
        }

        public String getTotal() {
            return mTotal;
        }

        public List<Photo> getPhotos() {
            return mPhotos;
        }
    }

    public class Photo {
        @SerializedName("id")
        private String mId;
        @SerializedName("url_s")
        private String mUrl;

        public String getId() {
            return mId;
        }

        public String getUrl() {
            return mUrl;
        }
    }
}
