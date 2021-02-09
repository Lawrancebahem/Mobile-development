package com.example.madlevel4task1.Fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel4task1.Adapter.ShoppingListAdapter
import com.example.madlevel4task1.DAO.ProductRepository
import com.example.madlevel4task1.Model.Product
import com.example.madlevel4task1.R
import com.example.madlevel4task1.databinding.FragmentShoppingListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ShoppingListFragment : Fragment() {

    private var _binding: FragmentShoppingListBinding? = null
    private var productList = arrayListOf<Product>()
    private val shoppingListAdapter = ShoppingListAdapter(productList)
    private lateinit var productRepository:ProductRepository

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        binding.deleteBtn.setOnClickListener{
            onDeleteClick()
        }

        binding.addBtn.setOnClickListener{
            showAddProductDialog()
        }
    }

    fun initViews() {
        binding.rcView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rcView.adapter = shoppingListAdapter

        productRepository = ProductRepository(requireContext())
        onSwipe().attachToRecyclerView(binding.rcView)

        //Get all products from the database
        getProductsFromDatabase()
    }



    fun getProductsFromDatabase(){
        productList.clear()
        CoroutineScope(Dispatchers.Main).launch {
            val products = withContext(Dispatchers.IO){
                productRepository.getAllProducts()
            }
            productList.addAll(products)
            shoppingListAdapter.notifyDataSetChanged()
        }
    }

    fun addProduct(productName:EditText, amount:EditText) {

        if (validateFields(productName, amount)){
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO){
                    val product = Product(productName.text.toString(), amount.text.toString().toInt())
                    productRepository.insertProduct(product)
                }
                getProductsFromDatabase()
            }
        }
    }

    fun onDeleteClick() {
        productList.clear()
        CoroutineScope(Dispatchers.Main).launch{
            withContext(Dispatchers.IO){
                productRepository.deleteAllProducts()
            }
            getProductsFromDatabase()
        }
    }

    fun showAddProductDialog(){

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.title))

        val dialogLayout = layoutInflater.inflate(R.layout.add_product_dialog, null)


        builder.setView(dialogLayout)

        val productName = dialogLayout.findViewById<EditText>(R.id.txt_product_name)
        val amount = dialogLayout.findViewById<EditText>(R.id.txt_amount)

        builder.setPositiveButton(R.string.btn_ok) { _: DialogInterface, _: Int ->
            addProduct(productName,amount)
        }
        builder.show()
    }


    fun validateFields(textProductName:EditText, textAmount: EditText):Boolean{
        return if(textProductName.text.toString().isNotEmpty() &&  textAmount.toString().isNotEmpty()){
            true
        }else{
            Toast.makeText(activity, "Please fill the fields", Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun onSwipe(): ItemTouchHelper {

        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO){
                        productRepository.deleteProduct(productList[position])
                    }
                    getProductsFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}