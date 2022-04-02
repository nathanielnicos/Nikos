package id.nns.nikos.utils

import android.content.Context
import androidx.preference.PreferenceManager
import id.nns.nikos.R

class SettingPreference(private val context: Context) {

    private val pref by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getMapState() : Boolean {
        return pref.getBoolean(context.getString(R.string.map), false)
    }

    fun getShareLocState() : Boolean {
        return pref.getBoolean("Location", false)
    }

    fun getWalletEditAndDeleteFeatures() : String {
        return pref.getString(context.getString(R.string.wallet), "long_press") ?: "long_press"
    }

    fun getPostNumber() : Int {
        return pref.getInt(context.getString(R.string.dashboard), 4)
    }

}
