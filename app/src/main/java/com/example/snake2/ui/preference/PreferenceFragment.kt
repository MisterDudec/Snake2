package com.example.snake2.ui.preference

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.domain.config.GLOW
import com.example.domain.config.START_SNAKE_LENGTH
import com.example.domain.config.START_SNAKE_LENGTH_DEFAULT
import com.example.snake2.R


class PreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(preferences: SharedPreferences?, key: String?) {
        if (preferences != null) {
            when (key) {
                activity?.getString(R.string.glow_key) -> {
                    GLOW = preferences.getBoolean(key, false)
                }
                activity?.getString(R.string.snake_length_key) -> {
                    START_SNAKE_LENGTH = preferences.getInt(key, START_SNAKE_LENGTH_DEFAULT)
                }
            }
        }
    }
}