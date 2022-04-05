package com.pg.homeworknetwork.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pg.homeworknetwork.data.Client
import com.pg.homeworknetwork.data.Movies
import com.pg.homeworknetwork.R
import com.pg.homeworknetwork.ui.recycler.MovieItemAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private lateinit var loading: View
    private lateinit var recycler: RecyclerView

//    private lateinit var adapter: MovieItemAdapter
//    private lateinit var endlessScroll: EndlessScrollListener
//    private val movies = mutableListOf<Movies.Short>()

    private val goToDetails = object : MovieItemAdapter.IOnItemClick {
        override fun onItemClick(movie: Movies.Short) {
            val manager: FragmentManager = parentFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            val detailsFragment = MovieDetailFragment()
            detailsFragment.arguments =
                Bundle().apply { putInt(MovieDetailFragment.ARG_ID, movie.id) }
            transaction.replace(R.id.mainFragment, detailsFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loading = view.findViewById(R.id.loading)

        recycler = view.findViewById<RecyclerView>(R.id.recycler).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MovieItemAdapter().apply { setClickListener(goToDetails) }
        }

        load()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun load() {
        Client.popular()
            .subscribeOn(Schedulers.io())
            .map(Movies::moviesShort)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onListLoaded, Throwable::printStackTrace)
    }

    private fun onListLoaded(list: List<Movies.Short>) {
        (recycler.adapter as MovieItemAdapter).submitList(list)
    }

    companion object {
        const val TAG = "MovieListFragment"
    }
}