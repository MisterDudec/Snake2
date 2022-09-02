package com.example.snake2.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import androidx.preference.DialogPreference
import androidx.preference.PreferenceDialogFragmentCompat
import com.example.snake2.activity.MainActivity
import com.example.snake2.R

class SettingsDialogFragment :  PreferenceDialogFragmentCompat() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).hideSystemBars()



        setStyle(
            STYLE_NORMAL,
            R.style.FullScreenDialogStyle
        )

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_dialog, container, false)
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        TODO("Not yet implemented")
    }
}