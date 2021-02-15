package com.example.madlevel5task2.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.madlevel5task2.databinding.FragmentAddGameBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddGameFragment : Fragment() {

    private val successMessage = "The game has been added"
    private var _binding: FragmentAddGameBinding? = null

    private val gameViewModel:GameViewModel  by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { _binding = FragmentAddGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabSave.setOnClickListener{
            onAddNewGame()
        }

    }


    fun onAddNewGame(){

        gameViewModel.insertGame(binding.addTitle, binding.addPlatform, binding.addDay, binding.addMonth, binding.addYear)

        gameViewModel.error.observe(viewLifecycleOwner){message ->
            Snackbar.make(binding.addConst, message, Snackbar.LENGTH_SHORT).show()
        }

        gameViewModel.sucsess.observe(viewLifecycleOwner){
            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}