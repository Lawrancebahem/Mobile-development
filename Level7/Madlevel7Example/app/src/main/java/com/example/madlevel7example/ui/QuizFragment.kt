package com.example.madlevel7example.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.madlevel7example.databinding.FragmentQuizBinding
import com.example.madlevel7example.viewModel.QuizViewModel


class QuizFragment : Fragment() {

    private val viewModel: QuizViewModel by activityViewModels()
    private lateinit var binding:FragmentQuizBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeQuiz()
    }

    @SuppressLint("LongLogTag")
    private fun observeQuiz() {
        viewModel.quiz.observe(viewLifecycleOwner) {
            val quiz = it
            binding.tvQuizQuestion.text = quiz.question

            binding.btConfirmAnswer.setOnClickListener {
                if (viewModel.isAnswerCorrect(binding.etQuizAnswer.text.toString())) {
                    Toast.makeText(context, "Your answer is correct!", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(
                        context,
                        "Your answer is incorrect! The correct answer was: ${quiz.answer}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
