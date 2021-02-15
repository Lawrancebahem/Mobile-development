package com.example.madlevel5task1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.madlevel5task1.databinding.FragmentAddNoteBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: NoteViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveBtn.setOnClickListener{
            saveNote()
        }
        observeNote()
    }

    private fun observeNote() {
        viewModel.note.observe(viewLifecycleOwner) { note ->
            note?.let {
                binding.title.setText(it.title)
                binding.note.setText(it.text)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { message ->
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.success.observe(viewLifecycleOwner){
            findNavController().popBackStack()
        }
    }

    private fun saveNote() {
        viewModel.updateNote(
                binding.title.text.toString(),
                binding.note.text.toString()
        )
    }
}