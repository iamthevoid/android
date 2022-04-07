package com.pg.homeworknetwork.ui.movie

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.RoundedCornersTransformation
import com.pg.homeworknetwork.BuildConfig
import com.pg.homeworknetwork.data.Movie
import com.pg.homeworknetwork.R

class MovieDetailFragment : Fragment(R.layout.fragment_movie_preview), MovieDetailView {

    private lateinit var poster: ImageView
    private lateinit var originalTitle: TextView
    private lateinit var overview: TextView
    private lateinit var popularity: TextView
    private lateinit var releaseDate: TextView

    private lateinit var presenter: MovieDetailPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            poster = findViewById(R.id.poster)
            originalTitle = findViewById(R.id.originalTitle)
            overview = findViewById(R.id.overview)
            popularity = findViewById(R.id.popularity)
            releaseDate = findViewById(R.id.releaseDate)
        }

        presenter = MovieDetailPresenter(requireArguments().getInt(ARG_ID), this)
        presenter.onCreate()
    }

    override fun onMovieDataReceived(movie: Movie) {
        poster.load("${BuildConfig.API_IMAGE_BASE_URL}${movie.posterPath}") {
            transformations(RoundedCornersTransformation(16f))
        }
        originalTitle.text = movie.originalTitle
        overview.text = movie.overview
        popularity.text = movie.popularity.toString()
        releaseDate.text = movie.releaseDate
    }

    override fun onTitleCalculated(titleRes: Int, title: String) {
        originalTitle.text = getString(titleRes, title)
    }

    companion object {
        const val TAG = "MovieDetailFragment"
        const val ARG_ID = "MovieDetailFragment_Arguments_Movie_Id"
    }
}