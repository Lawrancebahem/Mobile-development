package com.example.madlevel6task2.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.madlevel6task2.R
import com.example.madlevel6task2.adapter.MovieAdapter
import com.example.madlevel6task2.databinding.FragmentMovieDetailBinding
import com.example.madlevel6task2.databinding.FragmentMovieOverviewBinding
import com.example.madlevel6task2.model.Movie
import com.example.madlevel6task2.vm.MovieViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MovieOverviewFragment : Fragment() {

    private var _binding: FragmentMovieOverviewBinding? = null
    private val binding get() = _binding!!
    private val movieViewModel:MovieViewModel by activityViewModels()

    private val movieList = ArrayList<Movie>()

    private lateinit var movieAdapter:MovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMovieOverviewBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieAdapter = MovieAdapter(movieList, ::onMovieClick)
        initView()


        binding.btnSubmit.setOnClickListener{
            onSubmit()
        }
    }

    /**
     * Observe movie, set layout and adapter for recyclerView
     */
    private fun initView(){
        observeMovies()
        binding.rcView.layoutManager = GridLayoutManager(context,2)
        binding.rcView.adapter = movieAdapter
    }

    private fun onSubmit(){
        if (binding.overviewYear.text.isEmpty()){
            Snackbar.make(binding.root,getString(R.string.yearRequired), Snackbar.LENGTH_SHORT).show()
        }else{
            val year = binding.overviewYear.text.toString().toInt()
            movieViewModel.getMoviesList(year)
        }
    }

    /**
     * When user clicks a movie
     */
    private fun onMovieClick(movie:Movie){
        movieViewModel.setMovie(movie)
        findNavController().navigate(R.id.action_movieOverviewFragment2_to_movieDetailFragment2)
    }

    /**
     * Add the observed movies to the movie list
     */
    private fun observeMovies(){
        movieViewModel.moviesList.observe(viewLifecycleOwner){
            movieList.clear()
            movieList.addAll(it)
            movieAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}