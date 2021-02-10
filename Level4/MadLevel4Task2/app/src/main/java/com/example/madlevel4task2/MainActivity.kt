package com.example.madlevel4task2

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.madlevel4task2.DAO.HistoryRepository
import com.example.madlevel4task2.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var me:Menu
    private lateinit var historyRepository: HistoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        historyRepository = HistoryRepository(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        this.me = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.history -> onHistory()
            else ->
                super.onOptionsItemSelected(item)
        }
        return true
    }

    /**
     * Whem the history button gets clicked
     */
    fun onHistory(){
        findNavController(R.id.nav_host).navigate(R.id.action_gameFragment_to_histroyFragment)
    }

    /**
     * When the delete button gets clicked
     */
//    fun onDelete(){
//        findNavController(R.id.nav_host).navigate(R.id.action_gameFragment_to_histroyFragment)
//        this.me.findItem(R.id.history).isVisible = true
//        this.me.findItem(R.id.delete).isVisible = false
//
//        CoroutineScope(Dispatchers.Main).launch {
//            withContext(Dispatchers.IO){
//                historyRepository.deleteAllHistories()
//            }
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}