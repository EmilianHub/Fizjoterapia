package com.example.fizjotherapy.ui.login

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.fizjotherapy.R
import com.example.fizjotherapy.boundry.DbHelper
import com.example.fizjotherapy.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DbHelper(applicationContext)

        val toolbar = binding.appBarLogin.toolbar
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayoutLogin

        navController = findNavController(R.id.nav_host_fragment_content_login)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_moje_wizyty), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_login)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}