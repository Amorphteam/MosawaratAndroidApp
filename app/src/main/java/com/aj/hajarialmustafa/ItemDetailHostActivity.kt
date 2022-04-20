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
        connectionListener()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
    }



    private fun connectionListener() {
        Log.i("AJC", "111")
        val connectionManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectionManager.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                it.registerDefaultNetworkCallback(object: ConnectivityManager.NetworkCallback(){
                    override fun onAvailable(network: Network) {
                        checkOnlineJson()
                    }
                })
            }
        }
    }

    fun checkOnlineJson(){
        if (true){
            getMakhtotItemsOnline()
        }
    }

    @SuppressLint("CheckResult")
    private fun getMakhtotItemsOnline() {
        ServiceGenerator.requestApi.getAllMakhtotItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSubscriber<MakhtotItem>(){
                override fun onComplete() {
                    Toast.makeText(this@ItemDetailHostActivity, getString(R.string.getData), Toast.LENGTH_LONG).show()
                }

                override fun onNext(t: MakhtotItem?) {
                    val json: String = Gson().toJson(t?.posts)
                    PrefManagerSync.getInstance(this@ItemDetailHostActivity)?.setLocalJson(json)
                }

                override fun onError(t: Throwable?) {
                    Toast.makeText(this@ItemDetailHostActivity, getString(R.string.errorGettingData), Toast.LENGTH_LONG).show()
                    Log.i("AJC", t?.message.toString())
                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_item_detail)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}