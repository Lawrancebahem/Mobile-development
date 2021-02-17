package com.example.madlevel6task2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madlevel6task2.R
import com.example.madlevel6task2.api.MovieApi
import com.example.madlevel6task2.databinding.MovieBinding
import com.example.madlevel6task2.model.Movie

class MovieAdapter(private val movies:ArrayList<Movie>, val  onclick: (Movie) -> Unit):RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){

    //To use the context for Glide
    private lateinit var context:Context

    inner class MovieViewHolder(view:View):RecyclerView.ViewHolder(view){
        private val binding = MovieBinding.bind(view)
        init {
            itemView.setOnClickListener{
                onclick(movies[adapterPosition])
            }
        }

        fun bindData(movie:Movie, movieNumber:Int){
            binding.numMovie.text = (movieNumber + 1).toString()
            Glide.with(context).load(MovieApi.IMAGE_VIEW_URL + movie.posterPath).into(binding.movieImage)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        context = parent.context
        return MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.movie,parent,false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindData(movies[position], position)
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}