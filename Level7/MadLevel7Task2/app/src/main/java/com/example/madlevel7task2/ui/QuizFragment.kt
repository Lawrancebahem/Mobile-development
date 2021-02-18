package com.example.madlevel7task2.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.madlevel7task2.Model.Quiz
import com.example.madlevel7task2.R
import com.example.madlevel7task2.databinding.FragmentQuizBinding
import com.example.madlevel7task2.viewModel.QuizViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val quizViewModel: QuizViewModel by activityViewModels()
    private lateinit var quizList: ArrayList<Quiz>
    private var currentQuestionIndex = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root

    }


    private fun observeQuizzes() {
        quizViewModel.quizList.observe(viewLifecycleOwner) {
            quizList = it
            getNextQuestion()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeQuizzes()
        binding.confrimBtin.setOnClickListener {
            onConfirmClick()
        }
    }

    /**
     * To handle click on confirm button
     */
    private fun onConfirmClick() {
        val currentQuestion = quizList[currentQuestionIndex]
        if (checkAnswer(currentQuestion.goodAnswer!!)) {
            currentQuestionIndex++

            Handler().postDelayed(
                {
                    binding.radioGroup.clearCheck()
                    getNextQuestion()
                },
                2000
            )
            showToastMessage(getString(R.string.correct))
        } else {
            showToastMessage(getString(R.string.wrong))
        }
    }

    /**
     * To check the answer
     */
    private fun checkAnswer(goodAnswer: Int): Boolean {
        return when (binding.radioGroup.checkedRadioButtonId) {
            R.id.radio_one -> 0 == goodAnswer
            R.id.radio_two -> 1 == goodAnswer
            else -> 2 == goodAnswer
        }
    }

    /**
     * To get the next question based on the currentPosition
     */
    private fun getNextQuestion() {

        if (currentQuestionIndex < quizList.size) {
            val firstQuestion = quizList[currentQuestionIndex]
            binding.currentPage.text = (getString(R.string.current_page, currentQuestionIndex + 1))
            binding.question.text = firstQuestion.question
            binding.radioOne.text = firstQuestion.answersList!![0]
            binding.radioTwo.text = firstQuestion.answersList[1]
            binding.radioThree.text = firstQuestion.answersList[2]
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}