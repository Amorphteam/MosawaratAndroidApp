package com.aj.hajarialmustafa

import android.annotation.SuppressLint
import android.content.Context
import android.content.ServiceConnection
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.aj.hajarialmustafa.api.ServiceGenerator
import com.aj.hajarialmustafa.databinding.ActivityItemDetailBinding
import com.aj.hajarialmustafa.model.MakhtotItem
import com.aj.hajarialmustafa.model.Post
import com.aj.hajarialmustafa.placeholder.MainItems
import com.aj.hajarialmustafa.placeholder.MainItems.Companion.chechForUpdate
import com.aj.hajarialmustafa.preferences.PrefManagerSync
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber

class ItemDetailHostActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL;
        val binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        chechForUpdate(this)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_item_detail)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}