package com.pg.homeworknetwork.data

import com.pg.homeworknetwork.BuildConfig
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


object Client {

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private val api: Api by lazy { retrofit.create(Api::class.java) }

    // Api

    fun popular(page: Int = 1): Single<Movies> {
        return api.popular(page = page)
    }

    fun movie(id: Int): Single<Movie> {
        return api.movie(id)
    }
}

interface Api {

    companion object {
        const val API_VERSION = 3
    }

    @GET("/$API_VERSION/movie/popular")
    fun popular(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Single<Movies>

    @GET("/$API_VERSION/movie/{id}")
    fun movie(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Single<Movie>
}