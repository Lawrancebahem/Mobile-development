package com.example.shop.ui.profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shop.databinding.FragmentMyAdvertisementBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MyAdvertisementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyAdvertisementFragment : Fragment() {


    private lateinit var binding: FragmentMyAdvertisementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyAdvertisementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}