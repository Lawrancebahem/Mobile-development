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

    private fun initView(){
        observeMovies()
        binding.rcView.layoutManager = GridLayoutManager(context,2)
        binding.rcView.adapter = movieAdapter
    }

    fun onSubmit(){
        val year = binding.overviewYear.text.toString().toInt()
        movieViewModel.getMoviesList(year)
    }

    fun onMovieClick(movie:Movie){
        movieViewModel.setMovie(movie)
        Log.d("The clicked Movie", movie.toString())
        findNavController().navigate(R.id.action_movieOverviewFragment2_to_movieDetailFragment2)
    }

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