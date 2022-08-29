package com.example.snake2.ui.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.get
import com.example.domain.config.*
import com.example.domain.gamestate.GameState
import com.example.domain.gamestate.GameStateController
import com.example.domain.gamestate.GameStateController.gameState
import com.example.domain.gamestate.GameStateControllerObserver
import com.example.snake2.R
import com.example.snake2.activity.GameViewModel
import com.example.snake2.databinding.FragmentGameBinding
import kotlinx.coroutines.launch

@SuppressLint("NewApi")
class GameFragment : Fragment(), GameStateControllerObserver {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (gameState) {
                    GameState.Play -> GameStateController.pauseGame()
                    GameState.Pause -> activity?.onBackPressed()
                    GameState.GameOver -> activity?.onBackPressed()
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

        GameStateController.observer = this

        viewModel.liveData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                binding.appleCounter.text = it.appleCounter.toString()
                binding.liveCounter.text = it.liveCounter.toInt().toString()
            }
        }

        setButtonsListeners()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setButtonsListeners() {
        binding.topButton.directionButtonSetOnTouchListener(Direction.Top)
        binding.rightButton.directionButtonSetOnTouchListener(Direction.Right)
        binding.bottomButton.directionButtonSetOnTouchListener(Direction.Bottom)
        binding.leftButton.directionButtonSetOnTouchListener(Direction.Left)

        binding.pause.setOnClickListener {
            GameStateController.pauseGame(); controlButtonClick(it)
        }
        //TODO merge setters to extension
    }

    private fun View.directionButtonSetOnTouchListener(direction: Direction) {
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

    private fun controlButtonClick(it: View) {
        it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        it.startAnimation(AnimationUtils.loadAnimation(it.context, android.R.anim.fade_out))
    }

    override fun pauseGame() {
        with (findNavController()) {
            if (currentDestination == graph[R.id.GameFragment])
                navigate(R.id.action_GameFragment_to_MainMenuFragment)
        }
    }

    override fun gameOver() {
        lifecycleScope.launch {
            findNavController().navigate(R.id.action_GameFragment_to_GameOverFragment)
        } // Coroutine is is used to do not touch view from game thread
    }

    override fun resumeGame() {
        //showScreen()
    } //TODO merge these functions, create special observer for fragment and for the other classes

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        GameStateController.pauseGame()
        super.onPause()
    }

    override fun onResume() {
        GameStateController.resumeGame()
        super.onResume()
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