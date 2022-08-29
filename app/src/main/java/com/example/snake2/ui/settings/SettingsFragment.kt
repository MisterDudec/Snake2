package com.example.snake2.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.snake2.activity.MainActivity
import com.example.snake2.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        (activity as MainActivity).hideSystemBars()


        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}