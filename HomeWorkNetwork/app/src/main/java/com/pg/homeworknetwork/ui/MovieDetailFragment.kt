package com.pg.homeworknetwork.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import coil.load
import coil.transform.RoundedCornersTransformation
import com.pg.homeworknetwork.BuildConfig
import com.pg.homeworknetwork.data.Client
import com.pg.homeworknetwork.data.Movie
import com.pg.homeworknetwork.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieDetailFragment : Fragment(R.layout.fragment_movie_preview) {

    private lateinit var poster: ImageView
    private lateinit var originalTitle: TextView
    private lateinit var overview: TextView
    private lateinit var popularity: TextView
    private lateinit var releaseDate: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            poster = findViewById(R.id.poster)
            originalTitle = findViewById(R.id.originalTitle)
            overview = findViewById(R.id.overview)
            popularity = findViewById(R.id.popularity)
            releaseDate = findViewById(R.id.releaseDate)
        }

        arguments?.getInt(ARG_ID)?.also { movieId ->
            Client.movie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::show, Throwable::printStackTrace)
        }
    }

    private fun show(movie: Movie) {
        poster.load("${BuildConfig.API_IMAGE_BASE_URL}${movie.posterPath}") {
            transformations(RoundedCornersTransformation(16f))
        }
        originalTitle.text = movie.originalTitle
        overview.text = movie.overview
        popularity.text = movie.popularity.toString()
        releaseDate.text = movie.releaseDate
    }

    companion object {
        const val TAG = "MovieDetailFragment"
        const val ARG_ID = "MovieDetailFragment_Arguments_Movie_Id"
    }
}