package com.aj.hajarialmustafa.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences.RxSharedPreferences

class PrefManagerSync private constructor(ctx: Context) : IPrefManagerSync {
    private val rxPreferences: RxSharedPreferences
    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx)
    private val PREF_LOCAL_JSON = "PREF_LOCAL_JSON"

    companion object {
        private var instance: PrefManagerSync? = null
        fun getInstance(ctx: Context): PrefManagerSync? {
            if (instance == null) {
                instance = PrefManagerSync(ctx)
            }
            return instance
        }
    }

    init {
        rxPreferences = RxSharedPreferences.create(sharedPreferences)
    }

    override fun getLocalJson(): String? {
        return rxPreferences.getString(PREF_LOCAL_JSON).get()
    }

    override fun setLocalJson(localJson: String?) {
        rxPreferences.getString(PREF_LOCAL_JSON).set(localJson)
    }
}