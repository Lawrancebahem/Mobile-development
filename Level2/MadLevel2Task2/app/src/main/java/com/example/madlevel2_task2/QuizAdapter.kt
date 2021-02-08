package com.example.madlevel2_task2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel2_task2.databinding.QuizItemBinding


class QuizAdapter(private val quizzes: List<Quiz>, private val listener: OnItemClickListener) : RecyclerView.Adapter<QuizAdapter.ViewHolder>() {

    inner class ViewHolder(quizView: View) : RecyclerView.ViewHolder(quizView),View.OnClickListener {

//        click listener
        init {
            quizView.setOnClickListener(this)
        }

        private val binding = QuizItemBinding.bind(quizView)

        //Bind the text
        fun dataBind(quiz: Quiz) {
            binding.tvText.text = quiz.text
        }

        //from OnClick interface
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.quiz_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(quizzes[position])
    }

    override fun getItemCount(): Int {
        return quizzes.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}