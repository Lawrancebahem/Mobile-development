package com.example.madlevel5task1.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.madlevel5task1.R
import com.example.madlevel5task1.databinding.FragmentNotepadBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NotepadFragment : Fragment() {

    private var _binding: FragmentNotepadBinding? = null
    private val viewModel: NoteViewModel by viewModels()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentNotepadBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerNoteResult()
    }

    private fun observerNoteResult(){
        viewModel.note.observe(viewLifecycleOwner){note->
            note?.let {
                binding.noteTile.text = it.title;
                binding.tvLastUpdated.text = getString(R.string.lastUpdated, it.lastUpdated.toString())
                binding.noteText.text = it.text
            }
        }
    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}