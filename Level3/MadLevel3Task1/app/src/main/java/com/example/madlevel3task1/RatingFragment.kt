package com.example.madlevel3task1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.madlevel3task1.databinding.FragmentRatingBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RatingFragment : Fragment() {

    private var _binding: FragmentRatingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showRandomAssessableGame()

        binding.summaryBtn.setOnClickListener {
            navigateTosummary()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun showRandomAssessableGame() {
        val randomGame =
            listOf("Red Dead Redemption 2", "Rocket League", "Shadow of the Tombraider")
                    .random()
        binding.gameName.text = randomGame
    }


    fun navigateTosummary() {
        val args = Bundle()
        args.putFloat(ARG_GAME_RATING, binding.ratingBarGame.rating)
        args.putString(ARG_GAME_NAME, binding.gameName.text.toString())

        findNavController().navigate(R.id.action_ratingFragment2_to_summaryFragment, args)
    }
}