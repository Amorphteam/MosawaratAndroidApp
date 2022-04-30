package com.aj.hajarialmustafa.placeholder

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import android.widget.Toast
import com.aj.hajarialmustafa.R
import com.aj.hajarialmustafa.api.ServiceGenerator
import com.aj.hajarialmustafa.model.MakhtotItem
import com.aj.hajarialmustafa.model.Post
import com.aj.hajarialmustafa.model.Update
import com.aj.hajarialmustafa.preferences.PrefManagerSync
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */

interface DataListiner {
    fun onGetNewList()
}

class MainItems() {

    companion object {
        var dataListiner: DataListiner? = null
        fun chechForUpdate(context: Context, dataListiner: DataListiner) {
            this.dataListiner = dataListiner
            hasNetwork(context)
        }

        fun hasNetwork(context: Context) {
            val connectionManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectionManager.let {
                it.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        hasNewUpdate(context)
                    }

                })

            }
        }


        @SuppressLint("CheckResult")
        fun hasNewUpdate(context: Context) {
            ServiceGenerator.requestApi.getJsonLastUpdateDate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<Update>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: Update?) {
                        val lastDate = PrefManagerSync.getInstance(context)?.getLastDateUpdateJson()
                        if (t?.date != lastDate) {
                            PrefManagerSync.getInstance(context)?.setLastDateUpdateJson(t?.date!!)
                            getMakhtotItemsOnlineAndSaveToLocal(context)
                        }
                    }

                    override fun onError(t: Throwable?) {
                        Log.e("AJC", t?.message.toString())
                    }
                })

        }


        @SuppressLint("CheckResult")
        fun getMakhtotItemsOnlineAndSaveToLocal(context: Context) {
            ServiceGenerator.requestApi.getAllMakhtotItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<MakhtotItem>() {
                    override fun onComplete() {
                        if (dataListiner != null) {
                            dataListiner?.onGetNewList()
                        }
                    }

                    override fun onNext(t: MakhtotItem?) {
                        val json: String = Gson().toJson(t?.posts)
                        PrefManagerSync.getInstance(context)?.setLocalJson(json)
                    }

                    override fun onError(t: Throwable?) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.errorGettingData),
                            Toast.LENGTH_LONG
                        ).show()
                        Log.i("AJC", t?.message.toString())
                    }
                })
        }

        fun getOfflineMakhtotItemAsAList(context: Context): List<Post>? {
            val localJson: String? = PrefManagerSync.getInstance(context)?.getLocalJson()
            return Gson().fromJson(localJson, Array<Post>::class.java).toMutableList()
        }
    }


}