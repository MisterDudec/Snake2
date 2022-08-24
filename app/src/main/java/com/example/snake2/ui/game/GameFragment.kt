package com.example.snake2.ui.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import com.example.snake2.R
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
    //var p: Presenter? = null //toDelete

    private val viewModel by viewModels<GameViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveData.observe(viewLifecycleOwner) {
            binding.surfaceView.drawFrame(it)
            Log.d("test", "top = ${it.snake[0].rect.top}")
        }

        setButtons()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun prepareGame(view: View) {
        //viewModel.setDimensions()

//        binding.surfaceView.prepareGame(presenter)
        //setButtons(presenter)
        findNavController().navigate(R.id.action_GameFragment_to_MainMenuFragment)

        val navBackStackEntry = findNavController().getBackStackEntry(R.id.MainMenuFragment)

        navBackStackEntry.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
//                p?.let { startGame(it) }
                Log.d("GameFragment", "dialog destroyed")
            }
        })
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
            pauseGame(it)
        }
        binding.speedButton.setOnClickListener {
            //viewModel.thread.changeTicks()
            click(it)
        }
    }

    fun pauseGame(it: View) {
        if (!viewModel.isPaused()) {
            (it as AppCompatImageButton).setImageResource(R.drawable.ic_baseline_play_arrow_24)
            viewModel.pauseGame()
            /*findNavController().navigate(R.id.action_GameFragment_to_MainMenuFragment)
            binding.groupGameButtons.visibility = View.INVISIBLE
            val navBackStackEntry = findNavController().getBackStackEntry(R.id.MainMenuFragment)

            navBackStackEntry.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    Log.d("GameFragment", "dialog destroyed")
                    if (p != null) {
                        p!!.startGame()
                        Log.d("GameFragment", "dialog destroyed")
                    } else {
                        Log.d("GameFragment", "presenter is null")
                    }
                    //p?.let { startGame(it) }
                }
            })*/
        } else {
            (it as AppCompatImageButton).setImageResource(R.drawable.ic_baseline_pause_24)
            viewModel.resumeGame()
        }
        it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        it.startAnimation(AnimationUtils.loadAnimation(it.context, android.R.anim.fade_out))
    }

    /*
    private fun startGame(presenter: Presenter) {
        binding.groupGameButtons.visibility = View.VISIBLE
        presenter.resumeGame()
    }*/

    private fun click(it: View) {
        it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        it.startAnimation(AnimationUtils.loadAnimation(it.context, android.R.anim.fade_in))
    }

    override fun onDestroyView() {
        //binding.surfaceView.surfaceDestroyed(binding.surfaceView.holder)
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        //binding.surfaceView.surfaceDestroyed(binding.surfaceView.holder)
        super.onPause()
        //binding.surfaceView.
    }

    override fun onResume() {
        super.onResume()
        Log.d("GameFragment", "resume")
       // p?.let { startGame(it) }
        //binding.surfaceView.onResume()
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