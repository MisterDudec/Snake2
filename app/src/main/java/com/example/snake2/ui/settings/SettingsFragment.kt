package com.example.snake2.ui.settings

import android.app.ActionBar
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.marginTop
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceRecyclerViewAccessibilityDelegate
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.config.GLOW
import com.example.domain.config.START_SNAKE_LENGTH
import com.example.domain.config.START_SNAKE_LENGTH_DEFAULT
import com.example.snake2.R
import com.example.snake2.activity.MainActivity


class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(com.example.snake2.R.xml.root_preferences)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = super.onCreateView(inflater, container, savedInstanceState)

        //view?.setPadding(0, 256, 0, 64)
        view?.foregroundGravity = Gravity.CENTER_VERTICAL
        //view.be
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(Color.argb(50, 50, 50, 50))
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