package id.nns.nikos.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import id.nns.nikos.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

}
