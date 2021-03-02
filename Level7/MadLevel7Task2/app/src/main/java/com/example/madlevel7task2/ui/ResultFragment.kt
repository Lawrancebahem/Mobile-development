package com.example.madlevel7task2.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.madlevel7task2.R
import com.example.madlevel7task2.databinding.FragmentResultBinding
import com.example.madlevel7task2.viewModel.QuizViewModel

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val TOTAL_QUESTIONS = 10
    private val quizViewModel: QuizViewModel by activityViewModels()
    private val handler = Handler()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar()

        binding.resetBtn.setOnClickListener {
            onReset()
        }
    }

    /**
     * Set the progress bar
     */
    private fun progressBar() {
        quizViewModel.wrongAnsweredQuestions.observe(viewLifecycleOwner) { wrongAnsweredQuestion ->
            val totalGoodAnswers = TOTAL_QUESTIONS - wrongAnsweredQuestion.size
            val percentage = totalGoodAnswers * TOTAL_QUESTIONS
            val progressAnimator: ObjectAnimator = ObjectAnimator.ofInt(binding.progressBar, "progress", 0 , percentage)

            progressAnimator.duration = 2000
            progressAnimator.interpolator = DecelerateInterpolator()
            progressAnimator.start()

            binding.textViewProgress.text = "${percentage}%"
            binding.totalGood.text = getString(R.string.total_good_anwers, totalGoodAnswers)

        }
    }


    /**
     * navigate back to the home
     */
    private fun onReset() {
        findNavController().navigate(R.id.action_resultFragment_to_homeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}