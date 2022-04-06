package com.pg.homeworknetwork.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pg.homeworknetwork.data.Client
import com.pg.homeworknetwork.data.Movies
import com.pg.homeworknetwork.R
import com.pg.homeworknetwork.ui.recycler.EndlessScrollListener
import com.pg.homeworknetwork.ui.recycler.MovieItemAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private lateinit var loading: View
    private lateinit var recycler: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var endlessScroll: EndlessScrollListener

    private lateinit var adapter: MovieItemAdapter
    private val movies = mutableListOf<Movies.Short>()

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
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            endlessScroll.resetState()
            load()
        }

        recycler = view.findViewById<RecyclerView>(R.id.recycler).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).also { layoutManager ->
                addOnScrollListener(object : EndlessScrollListener(layoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                        load(page + 1)
                    }
                }.also { endlessScroll = it })
            }
            adapter = MovieItemAdapter()
                .apply { setClickListener(goToDetails) }
                .also { this@MovieListFragment.adapter = it }
        }

        load()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun load(page: Int = FIRST_PAGE) {
        Client.popular(page)
            .subscribeOn(Schedulers.io())
            .map(Movies::moviesShort)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loading.visibility = View.VISIBLE }
            .doOnTerminate { loading.visibility = View.INVISIBLE }
            .subscribe(
                { pageContents -> onListLoaded(page, pageContents) },
                Throwable::printStackTrace
            )
    }

    private fun onListLoaded(page: Int, list: List<Movies.Short>) {
        swipeRefreshLayout.isRefreshing = false
        if (page == FIRST_PAGE) {
            movies.clear()
        }
        movies.addAll(list)
        adapter.submitList(movies)
    }

    companion object {
        const val TAG = "MovieListFragment"
        const val FIRST_PAGE = 1
    }
}