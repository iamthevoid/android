package com.pg.homeworknetwork.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pg.homeworknetwork.data.Movies
import com.pg.homeworknetwork.R
import com.pg.homeworknetwork.ui.movie.MovieDetailFragment
import com.pg.homeworknetwork.ui.recycler.EndlessScrollListener
import com.pg.homeworknetwork.ui.recycler.MovieItemAdapter

class MovieListFragment : Fragment(R.layout.fragment_movie_list), MoviesListView {

    private lateinit var loading: View
    private lateinit var recycler: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var adapter: MovieItemAdapter
    private lateinit var scrollListener: EndlessScrollListener

    private lateinit var presenter: MoviesListPresenter

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
            scrollListener.resetState()
            presenter.refresh()
        }

        recycler = view.findViewById<RecyclerView>(R.id.recycler).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).also {
                addOnScrollListener(object : EndlessScrollListener(it) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                        presenter.loadMore()
                    }
                }.also { scrollListener = it })
            }
            adapter = MovieItemAdapter().also {
                this@MovieListFragment.adapter = it
            }.apply { setClickListener(goToDetails) }
        }

        presenter = MoviesListPresenter(this)
        presenter.onCreate()

        super.onViewCreated(view, savedInstanceState)
    }


    override fun showMovies(list: List<Movies.Short>) {
        adapter.submitList(list)
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading.visibility = View.INVISIBLE
    }

    override fun showRefreshing() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideRefreshing() {
        swipeRefreshLayout.isRefreshing = false
    }


    companion object {
        const val TAG = "MovieListFragment"
    }
}