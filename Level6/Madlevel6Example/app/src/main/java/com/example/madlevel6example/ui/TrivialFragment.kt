package com.example.madlevel6example.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.madlevel6example.databinding.FragmentTrivialBinding
import com.example.madlevel6example.viewModel.TrivialViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TrivialFragment : Fragment() {

    private var _binding: FragmentTrivialBinding? = null

    // This property is only valid between onCreateView and

    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: TrivialViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTrivialBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeTrivia()

    }
    private fun observeTrivia() {
        viewModel.trivial.observe(viewLifecycleOwner, Observer {
            binding.tvTrivia.text = it?.text
        })

        // Observe the error message.
        viewModel.errorText.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}