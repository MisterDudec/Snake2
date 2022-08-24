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


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

@SuppressLint("NewApi")
class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<GameViewModel>()


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
            override fun surfaceCreated(p0: SurfaceHolder) {
                Log.d("threads", "surfaceCreated: ${Looper.myLooper()}")
                viewModel.setDimensions(binding.surfaceView.width, binding.surfaceView.height)
                viewModel.liveData.observe(viewLifecycleOwner) {
                    Log.d("observe", "onViewCreated looper: ${Looper.myLooper()}")
                    binding.surfaceView.drawFrame(it)
                }
                viewModel.startGame()
            }
            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

            }
            override fun surfaceDestroyed(p0: SurfaceHolder) {
            }
        }

        binding.surfaceView.holder.addCallback(callback)
        setButtons()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun resumeGame() {
        viewModel.resumeGame()
        binding.groupGameButtons.visibility = View.VISIBLE
        binding.startGame.visibility = View.GONE
    }

    fun pauseGame() {
        viewModel.pauseGame()
        binding.groupGameButtons.visibility = View.INVISIBLE
        binding.startGame.visibility = View.VISIBLE
    }

    private fun setButtons() {
        binding.topButton.setOnClickListener {
            viewModel.changeDirection(DIR_TOP)
            click(it)
        }
        binding.rightButton.setOnClickListener {
            viewModel.changeDirection(DIR_RIGHT)
            click(it)
        }
        binding.bottomButton.setOnClickListener {
            viewModel.changeDirection(DIR_BOTTOM)
            click(it)
        }
        binding.leftButton.setOnClickListener {
            viewModel.changeDirection(DIR_LEFT)
            click(it)
        }
        binding.pause.setOnClickListener {
            pauseGame()
            it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            it.startAnimation(AnimationUtils.loadAnimation(it.context, android.R.anim.fade_out))
        }
        binding.speedButton.setOnClickListener {
            //viewModel.thread.changeTicks()
            click(it)
        }
        binding.startGame.setOnClickListener {
            resumeGame()
            it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            it.startAnimation(AnimationUtils.loadAnimation(it.context, android.R.anim.fade_out))
        }
    }

    private fun click(it: View) {
        it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        it.startAnimation(AnimationUtils.loadAnimation(it.context, android.R.anim.fade_in))
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