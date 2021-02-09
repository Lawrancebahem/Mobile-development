package com.example.madlevel4task1.DAO

import android.content.Context
import com.example.madlevel4task1.Model.Product

class ProductRepository(context: Context) {

    private var productDao: ProductDAO

    //Get the databsae
    init {
        val productDatabase = ShoppingListRoomDatabase.getDatabase(context)
        productDao = productDatabase.productDao()
    }

    suspend fun getAllProducts(): List<Product> = productDao.getAllProduct()


    suspend fun insertProduct(product: Product) = productDao.insertProduct(product)


    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)

    suspend fun deleteAllProducts() = productDao.deleteAllProducts()

}