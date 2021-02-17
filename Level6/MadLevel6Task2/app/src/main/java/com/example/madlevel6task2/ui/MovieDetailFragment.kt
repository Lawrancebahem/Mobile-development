package com.example.madlevel6task2.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.madlevel6task2.api.MovieApi
import com.example.madlevel6task2.databinding.FragmentMovieDetailBinding
import com.example.madlevel6task2.vm.MovieViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding

    private val movieViewModel: MovieViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMovieDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMovie()

    }

    //Get the currentMovie
    private fun observeMovie() {
        movieViewModel.getMovie.value?.let { currentMovie ->
            binding.releaseDate.text = currentMovie.releaseDate
            binding.movieTitle.text = currentMovie.title
            binding.overviewText.text = currentMovie.overview
            binding.ratingAverage.text = currentMovie.voteAverage.toString()
            Glide.with(requireContext()).load(MovieApi.IMAGE_VIEW_URL + currentMovie.backdropPath)
                .into(binding.headerImage)
            Glide.with(requireContext()).load(MovieApi.IMAGE_VIEW_URL + currentMovie.posterPath)
                .into(binding.descriptionImage)
        }
    }

}