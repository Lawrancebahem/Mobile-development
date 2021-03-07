package com.example.shop.ui.profile.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shop.R
import com.example.shop.databinding.FragmentMyAdvertisementBinding
import com.example.shop.model.Product
import com.example.shop.model.User
import com.example.shop.ui.main.adapter.ProductAdapter
import com.example.shop.ui.main.viewModel.AdvertisementViewModel
import com.example.shop.ui.main.viewModel.UserDatabaseViewModel
import com.example.shop.ui.productPreview.ProductPreviewActivity
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [MyAdvertisementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MyAdvertisementFragment : Fragment() {

    private lateinit var binding: FragmentMyAdvertisementBinding

    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()
    private lateinit var userDatabaseViewModel: UserDatabaseViewModel
    private lateinit var userAddProducts: ArrayList<Product>
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
        binding = FragmentMyAdvertisementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init()
    }

    /**
     * getting all products of this user
     */
    private fun init() {
        userDatabaseViewModel = ViewModelProvider(this).get(UserDatabaseViewModel::class.java)
        currentUser = userDatabaseViewModel.userRepository.getUser()

        //get the added products by this user
        getUserAddedProducts()
        //get the liked product of this user
        val gridLayoutManager = GridLayoutManager(context, 2)
        binding.rcView.layoutManager = gridLayoutManager
        //observe the added and liked product of this user
        advertisementViewModel.userAddProducts.observe(viewLifecycleOwner) { addedProducts ->
            userAddProducts = ArrayList(addedProducts)

            //like products
            getUsersLikes()
            advertisementViewModel.userLikes.observe(viewLifecycleOwner) { userLikedProducts ->

                //observe the user
                currentUser.observe(viewLifecycleOwner) { user ->
                    productAdapter = ProductAdapter(
                        userAddProducts,
                        ::onProductClick,
                        ::addLike,
                        ::removeLike,
                        userLikedProducts,
                        user.id,
                        ::onDeleteProduct
                    )
                    binding.rcView.adapter = productAdapter
                }
            }
        }
    }

    /**
     * To preview the product when the user clicks on a certain product
     */
    private fun onProductClick(index: Int) {
        val selectedProduct = userAddProducts[index]
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
            advertisementViewModel.getUserLikes(it.id)
        }
    }


    /**
     * To get all user added products
     */
    private fun getUserAddedProducts() {
        currentUser.observe(viewLifecycleOwner) {
            advertisementViewModel.getUserProducts(it.id)
        }

    }

    /**
     * On delete a product
     */
    private fun onDeleteProduct(product:Product){
        val alertBuilder = advertisementViewModel.getConfirmationDialog(getString(R.string.sureDelete), requireContext())
        alertBuilder.setPositiveButton(getString(R.string.yes)) { dialog, id ->
            // Delete selected note from database
            productAdapter.productList.remove(product)
            (productAdapter.usersLikes as HashSet<Product>).remove(product)
            advertisementViewModel.removeProduct(product.id!!)
            productAdapter.notifyDataSetChanged()        }
        alertBuilder.create()
        alertBuilder.show()
    }

}