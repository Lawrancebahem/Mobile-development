package com.example.madlevel7example.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.madlevel7example.R
import com.example.madlevel7example.databinding.FragmentHomeBinding
import com.example.madlevel7example.viewModel.QuizViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val viewModel: QuizViewModel by activityViewModels()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        //always retrieve quiz  when screen is shown
        viewModel.getQuiz()

        binding.createBtnHome.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_createQuizFragment)
        }
        viewModel.quiz.observe(viewLifecycleOwner, Observer {
            //make button visible and clickable
            if (!it.answer.isBlank() || !it.answer.isBlank()) {
                binding.startQuizBtn.alpha = 1.0f
                binding.startQuizBtn.isClickable = true

                binding.startQuizBtn.setOnClickListener {
                    navController.navigate(R.id.action_homeFragment_to_quizFragment5)
                }
            }
        })
    }
}