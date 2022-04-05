package com.pg.homeworknetwork.data

import com.google.gson.annotations.SerializedName

/*
    {
      "adult": false,
      "backdrop_path": "/iQFcwSGbZXMkeyKrxbPnwnRo5fl.jpg",
      "genre_ids": [
        28,
        12,
        878
      ],
      "id": 634649,
      "original_language": "en",
      "original_title": "Spider-Man: No Way Home",
      "overview": "Peter Parker is unmasked and no longer able to separate his normal life from the high-stakes of being a super-hero. When he asks for help from Doctor Strange the stakes become even more dangerous, forcing him to discover what it truly means to be Spider-Man.",
      "popularity": 6646,
      "poster_path": "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
      "release_date": "2021-12-15",
      "title": "Spider-Man: No Way Home",
      "video": false,
      "vote_average": 8.2,
      "vote_count": 10971
    }
 */
data class Movies(

    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val moviesShort: List<Short>,

    @SerializedName("total_pages")
    val pages: Int,

    @SerializedName("total_results")
    val total: Int,
) {
    data class Short(

        @SerializedName("id")
        val id: Int,

        @SerializedName("title")
        val title: String,

        @SerializedName("poster_path")
        val poster: String,
    )
}

data class Movie(

    @SerializedName("original_title")
    val originalTitle: String,

    @SerializedName("poster_path")
    val posterPath: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("popularity")
    val popularity: Double,

    @SerializedName("release_date")
    val releaseDate: String,
)