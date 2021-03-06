package com.example.shop.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shop.R
import com.example.shop.adapter.ProductAdapter
import com.example.shop.databinding.FragmentHomeBinding
import com.example.shop.model.Product
import com.example.shop.viewModel.AdvertisementViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()
    private lateinit var userDatabaseViewModel: UserDatabaseViewModel
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


    /**
     * get the Singleton instance of the database
     */
    private fun init(){
        userDatabaseViewModel = ViewModelProvider(this).get(UserDatabaseViewModel::class.java)
        advertisementViewModel.getAllProducts()

        getUsersLikes()

        advertisementViewModel.productList.observe(viewLifecycleOwner) { products ->
            //load the liked products into the set array
            advertisementViewModel.userLikes.observe(viewLifecycleOwner) { likes ->
                productAdapter = ProductAdapter(products, ::onProductClick, ::addLike, ::removeLike, likes)
                binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
                binding.recyclerView.adapter = productAdapter
                productAdapter.notifyDataSetChanged()
            }
        }
    }

    /**
     * To preview the product when the user clicks on a certain product
     */
    private fun onProductClick(index:Int){
        advertisementViewModel.selectedProductIndex = index
        findNavController().navigate(R.id.productOverview)
    }

    /**
     * When the user clicks on likes button of a certain product, this method will get the productId
     */
    private fun addLike(productId:Long){
        CoroutineScope(Dispatchers.Main).launch {
            val user = withContext(Dispatchers.IO){
                userDatabaseViewModel.userRepository.getUser()
            }
            if (user.isNotEmpty()) {
                advertisementViewModel.insertLike(user[0].id, productId)
            }
        }
    }

    /**
     * To remove a like of the liked products of a user
     */
    private fun removeLike(productId:Long){
        CoroutineScope(Dispatchers.Main).launch {
            val user = withContext(Dispatchers.IO){
                userDatabaseViewModel.userRepository.getUser()
            }
            if (user.isNotEmpty()) {
                advertisementViewModel.removeLike(user[0].id, productId)
            }
        }
    }


    /**
     * to get user liked products
     */
    private fun getUsersLikes(){

        CoroutineScope(Dispatchers.Main).launch {
            val user = withContext(Dispatchers.IO){
                userDatabaseViewModel.userRepository.getUser()
            }
            if (user.isNotEmpty()) {
                advertisementViewModel.getUserLikes(user[0].id)
            }
        }
    }
}