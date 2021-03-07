package com.example.shop.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shop.R
import com.example.shop.databinding.FragmentHomeBinding
import com.example.shop.model.Product
import com.example.shop.model.User
import com.example.shop.ui.main.adapter.ProductAdapter
import com.example.shop.ui.main.viewModel.AdvertisementViewModel
import com.example.shop.ui.main.viewModel.UserDatabaseViewModel
import com.example.shop.ui.productPreview.ProductPreviewActivity
import dagger.hilt.android.AndroidEntryPoint


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
    private lateinit var productAdapter: ProductAdapter

    private lateinit var currentUser: LiveData<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        inflater.inflate(R.menu.menu_search, menu)
    }


    /**
     * get the Singleton instance of the database
     */
    private fun init() {
        userDatabaseViewModel = ViewModelProvider(this).get(UserDatabaseViewModel::class.java)
        currentUser = userDatabaseViewModel.userRepository.getUser()
        advertisementViewModel.getAllProducts()

        getUsersLikes()

        //observe the products
        advertisementViewModel.productList.observe(viewLifecycleOwner) { products ->
            //load the liked products but this user into the set array
            advertisementViewModel.userLikes.observe(viewLifecycleOwner) { likedProducts ->
                //observe the user
                currentUser.observe(viewLifecycleOwner) { user ->
                    var userId: Long = 0
                    if (user != null) {
                        userId = user.id
                    }
                    productAdapter = ProductAdapter(
                        products,
                        ::onProductClick,
                        ::addLike,
                        ::removeLike,
                        likedProducts,
                        userId,
                        ::onDeleteProduct
                    )
                    binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
                    binding.recyclerView.adapter = productAdapter
                    productAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    /**
     * To preview the product when the user clicks on a certain product
     */
    private fun onProductClick(index: Int) {
        val selectedProduct = advertisementViewModel.productList.value!![index]
        val intent = Intent(requireContext(), ProductPreviewActivity::class.java)
        intent.putExtra("selectedProductId", selectedProduct.id)
        startActivity(intent)
    }

    /**
     * When the user clicks on likes button of a certain product, this method will get the productId
     */
    private fun addLike(productId: Long) {
        currentUser.observe(viewLifecycleOwner) {
            advertisementViewModel.insertLike(it.id, productId)
        }
    }

    /**
     * To remove a like of the liked products of a user
     */
    private fun removeLike(productId: Long) {
        currentUser.observe(viewLifecycleOwner) {
            advertisementViewModel.removeLike(it.id, productId)
        }
    }


    /**
     * to get user liked products
     */
    private fun getUsersLikes() {
        currentUser.observe(viewLifecycleOwner) {
            var userId: Long = 0
            if (it != null) {
                userId = it.id
            }
            advertisementViewModel.getUserLikes(userId)
        }
    }

    /**
     * When the user clicks on the delete icon to remove a product
     */
    private fun onDeleteProduct(product: Product){
        val alertBuilder = advertisementViewModel.getConfirmationDialog(getString(R.string.sureDelete), requireContext())
        alertBuilder.setPositiveButton(getString(R.string.yes)) { dialog, id ->
            // Delete selected note from database

            productAdapter.productList.remove(product)
            (productAdapter.usersLikes as HashSet<Product>).remove(product)
            advertisementViewModel.removeProduct(product.id!!)
            productAdapter.notifyDataSetChanged()
        }
        alertBuilder.create()
        alertBuilder.show()
    }
}