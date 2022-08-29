package com.example.snake2.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.snake2.R
import com.example.snake2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemBars()
    }

    fun hideSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /*override fun onTouchEvent(event: MotionEvent): Boolean {

        return when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(DEBUG_TAG, "Action was DOWN")
                true
            }
            MotionEvent.ACTION_MOVE -> {
                event.
                Log.d(DEBUG_TAG, "Action was MOVE")
                true
            }
            MotionEvent.ACTION_UP -> {
                Log.d(DEBUG_TAG, "Action was UP")
                true
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d(DEBUG_TAG, "Action was CANCEL")
                true
            }
            MotionEvent.ACTION_OUTSIDE -> {
                Log.d(DEBUG_TAG, "Movement occurred outside bounds of current screen element")
                true
            }
            else -> super.onTouchEvent(event)
        }
    }*/

    /*
    val APP_PREFERENCES = "settings"

    val APP_PREFERENCES_UNCENSOREDMOD = "uncensoredMod"
    val APP_PREFERENCES_GODMOD = "godMod"

    var APP_PREFERENCES_GAMEMOD_VALUE = false
    var APP_PREFERENCES_GODMOD_VALUE = false
    var APP_PREFERENCES_SNAKEBODY_VALUE = 0
    var APP_PREFERENCES_CHERRY_VALUE = 0

    private val mainSettings: SharedPreferences? = null*/

    /*override fun onPause() {
        super.onPause()
        // Запоминаем данные
        val editor = mainSettings!!.edit()
        editor.putBoolean(APP_PREFERENCES_UNCENSOREDMOD, APP_PREFERENCES_GAMEMOD_VALUE)
        editor.putBoolean(APP_PREFERENCES_GODMOD, APP_PREFERENCES_GODMOD_VALUE)
        editor.putInt(resources.getString(R.string.pref_snakeBody), APP_PREFERENCES_SNAKEBODY_VALUE)
        editor.putInt(resources.getString(R.string.pref_cherries), APP_PREFERENCES_SNAKEBODY_VALUE)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        val s = "0"
        if (mainSettings!!.contains(APP_PREFERENCES_UNCENSOREDMOD)) APP_PREFERENCES_GAMEMOD_VALUE =
            mainSettings.getBoolean(APP_PREFERENCES_UNCENSOREDMOD, false)
        if (mainSettings.contains(APP_PREFERENCES_GODMOD)) APP_PREFERENCES_GODMOD_VALUE =
            mainSettings.getBoolean(APP_PREFERENCES_GODMOD, false)
        if (mainSettings.contains(resources.getString(R.string.pref_snakeBody))) APP_PREFERENCES_SNAKEBODY_VALUE =
            mainSettings.getInt(resources.getString(R.string.pref_snakeBody), 0)
        if (mainSettings.contains(resources.getString(R.string.pref_cherries))) APP_PREFERENCES_CHERRY_VALUE =
            mainSettings.getInt(resources.getString(R.string.pref_cherries), 0)
    }*/
}