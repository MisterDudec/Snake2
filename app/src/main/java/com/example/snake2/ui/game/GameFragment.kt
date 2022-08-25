package com.example.snake2.ui.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.snake2.databinding.FragmentGameBinding
import com.example.snake2.ui.game.GameViewModel.Companion.DIR_BOTTOM
import com.example.snake2.ui.game.GameViewModel.Companion.DIR_LEFT
import com.example.snake2.ui.game.GameViewModel.Companion.DIR_RIGHT
import com.example.snake2.ui.game.GameViewModel.Companion.DIR_TOP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

@SuppressLint("NewApi")
class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<GameViewModel>()

    sealed class State {
        class Pause : State()
        class Play : State()
        class GameOver : State()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isPaused()) {
                    activity?.onBackPressed()
                } else {
                    pauseGame()
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        Log.d("threads", "MainLooper: ${Looper.getMainLooper()}")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("threads", "onViewCreated: ${Looper.myLooper()}")

        val callback = object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                Log.d("threads", "surfaceCreated: ${Looper.myLooper()}")

                viewModel.setDimensions(binding.surfaceView.width, binding.surfaceView.height)
                viewModel.liveData.observe(viewLifecycleOwner) {
                    Log.d("observe", "onViewCreated looper: ${Looper.myLooper()}")

                    CoroutineScope(Dispatchers.Unconfined).launch {
                        binding.surfaceView.drawFrame(it)
                    }
                    binding.appleCounter.text = it.appleCounter.toString()
                    binding.liveCounter.text = it.liveCounter.toInt().toString()
                    if (it.gameOver) showGameOverScreen()
                }
                viewModel.startGame()
            }
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                viewModel.setDimensions(binding.surfaceView.width, binding.surfaceView.height)
            }
            override fun surfaceDestroyed(holder: SurfaceHolder) {

            }
        }

        binding.surfaceView.holder.addCallback(callback)
        setButtons()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun resumeGame() {
        viewModel.resumeGame()
        showScreen(State.Play())
    }

    private fun pauseGame() {
        viewModel.pauseGame()
        showScreen(State.Pause())
    }

    private fun showGameOverScreen() {
        showScreen(State.GameOver())
    }

    private fun restartGame() {
        viewModel.restartGame()
        showScreen(State.Play())
    }

    private fun showScreen(state: State) {
        binding.groupGameButtons.visibility = View.GONE
        binding.groupGameOverButtons.visibility = View.GONE
        binding.startGame.visibility = View.GONE

        when (state) {
            is State.Pause -> binding.startGame.visibility = View.VISIBLE
            is State.Play -> binding.groupGameButtons.visibility = View.VISIBLE
            is State.GameOver -> binding.groupGameOverButtons.visibility = View.VISIBLE
        }
    }


    private fun setButtons() {
        binding.topButton.directionButtonSetOnTouchListener(DIR_TOP)
        binding.rightButton.directionButtonSetOnTouchListener(DIR_RIGHT)
        binding.bottomButton.directionButtonSetOnTouchListener(DIR_BOTTOM)
        binding.leftButton.directionButtonSetOnTouchListener(DIR_LEFT)

        binding.pause.setOnClickListener {
            pauseGame(); controlButtonClick(it)
        }
        binding.startGame.setOnClickListener {
            resumeGame(); controlButtonClick(it)
        }
        binding.restartGame.setOnClickListener {
            restartGame(); controlButtonClick(it)
        }
    }

    private fun View.directionButtonSetOnTouchListener(direction: Int) {
        setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        viewModel.changeDirection(direction)
                        performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        performClick()
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun controlButtonClick(views: Array<View>) {
        views.forEach {
            it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            it.startAnimation(AnimationUtils.loadAnimation(it.context, android.R.anim.fade_out))
        }
    }

    private fun controlButtonClick(it: View) {
        it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        it.startAnimation(AnimationUtils.loadAnimation(it.context, android.R.anim.fade_out))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        pauseGame()
        super.onPause()
    }
}

/**
Лучше, чтобы передача UI поля потоку происходила посредством WeakReference (или где-то
устанавливался бы референс в null). Иначе, в случае зависания потока будет мемори лик
целой активити со всем наполнением потому как потоки не обязательно сразу удаляются
мусоросборщиком, а могут еще жить неопределенное время. В этом случае, если запускать
приложение несколько раз, размер свободного пространства на хипе может быстро сократиться,
что может привести к ООМ эксепшену.

Пишу это не потому что охото придраться к коду, а потому, что в своем большом проекте уже
большое кличество времени потратили на избавление от подобных мемори ликов.
 */