package com.example.madlevel6task1.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel6task1.adapter.ColorAdapter
import com.example.madlevel6task1.databinding.FragmentColorBinding
import com.example.madlevel6task1.model.ColorItem
import com.example.madlevel6task1.vm.ColorViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ColorFragment : Fragment() {

    private var _binding: FragmentColorBinding? = null
    private val binding get() = _binding!!

    private val colorList = arrayListOf<ColorItem>()
    private val colorViewModel:ColorViewModel by viewModels()
    private lateinit var colorAdapter:ColorAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentColorBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        colorAdapter = ColorAdapter(colorList, ::onColorClick)
        binding.rvColors.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvColors.adapter = colorAdapter

        observeColors()

    }

    private fun observeColors() {
        colorViewModel.colorItems.observe(viewLifecycleOwner, {
            colorList.clear()
            colorList.addAll(it)
            colorAdapter.notifyDataSetChanged()
        })
    }

    private fun onColorClick(colorItem: ColorItem) {
        Snackbar.make(binding.rvColors, "This color is: ${colorItem.name}", Snackbar.LENGTH_LONG)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}