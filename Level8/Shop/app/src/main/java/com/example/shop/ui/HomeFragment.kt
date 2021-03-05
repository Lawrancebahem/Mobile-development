package com.example.shop.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.R
import com.example.shop.adapter.ImageAdapter
import com.example.shop.adapter.ProductAdapter
import com.example.shop.databinding.FragmentHomeBinding
import com.example.shop.viewModel.AdvertisementViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding


    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()

    private lateinit var productAdapter:ProductAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search,menu)
    }


    private fun init(){
        advertisementViewModel.getAllProducts()

        advertisementViewModel.productList.observe(viewLifecycleOwner){
            productAdapter = ProductAdapter(it, ::onProductClick)
            binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
            binding.recyclerView.adapter = productAdapter
        }
    }


    private fun onProductClick(index:Int){
        advertisementViewModel.selectedProductIndex = index
        findNavController().navigate(R.id.productOverview)
    }
}