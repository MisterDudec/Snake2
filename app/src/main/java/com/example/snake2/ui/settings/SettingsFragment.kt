package com.example.snake2.ui.settings

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.preference.DialogPreference
import androidx.preference.PreferenceDialogFragmentCompat
import androidx.preference.PreferenceFragmentCompat
import com.example.snake2.activity.MainActivity
import com.example.snake2.R

class SettingsFragment() : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        (activity as MainActivity).hideSystemBars()

        setPreferencesFromResource(R.xml.root_preferences, rootKey)

    }
}