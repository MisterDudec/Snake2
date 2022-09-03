package com.example.snake2.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.domain.config.GLOW
import com.example.domain.config.GLOW_DEFAULT
import com.example.domain.config.START_SNAKE_LENGTH
import com.example.domain.config.START_SNAKE_LENGTH_DEFAULT
import com.example.snake2.R
import com.example.snake2.databinding.ActivityMainBinding
import com.example.snake2.surfaceview.SurfaceHolderCallback

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by viewModels()
    private var surfaceHolderCallback: SurfaceHolderCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        Log.d("threads", "onCreate(): ${Thread.currentThread()}")

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemBars()

        setPreferences()
    }

    override fun onStart() {
        super.onStart()
        surfaceHolderCallback = SurfaceHolderCallback(viewModel, binding.surfaceView, this)
        binding.surfaceView.holder.addCallback(surfaceHolderCallback)
        viewModel.registerViewModelObserver(binding.surfaceView)
    }

    override fun onStop() {
        viewModel.unregisterViewModelObserver()
        binding.surfaceView.holder.removeCallback(surfaceHolderCallback)
        surfaceHolderCallback = null
        super.onStop()
    }

    private fun setPreferences() {
        with (getDefaultSharedPreferences(this)) {
            GLOW = getBoolean(getString(R.string.glow_key), GLOW_DEFAULT)
            START_SNAKE_LENGTH = getInt(getString(R.string.snake_length_key), START_SNAKE_LENGTH_DEFAULT)
        }
    }

    private fun hideSystemBars() {
        with (WindowCompat.getInsetsController(window, window.decorView)) {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.systemBars())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
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