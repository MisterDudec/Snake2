package com.example.snake2.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import com.example.snake2.R
import com.example.snake2.data.GameFieldData
import com.example.snake2.databinding.FragmentGameBinding
import com.example.snake2.presenters.Presenter


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

@SuppressLint("NewApi")
class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    var p: Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ((activity as AppCompatActivity?)!!.supportActionBar)?.hide()

        binding.surfaceView.post { //post is necessary to wait till drawing phase of view and get actual dimensions
            val presenter = prepareGame(view)
            p = presenter
            Log.d("GameFragment", "post")
        }
    }
    private fun prepareGame(view: View) : Presenter {
        val backgroundColor = view.context.getColor(R.color.background_surface_view)
        val snakeColor = view.context.getColor(R.color.snake)
        val appleColor = view.context.getColor(R.color.apple)
        val presenter = Presenter(
            GameFieldData(view.measuredWidth, view.measuredHeight),
            backgroundColor,
            snakeColor,
            appleColor
        )

        binding.surfaceView.prepareGame(presenter)
        setButtons(presenter)
        findNavController().navigate(R.id.action_GameFragment_to_MainMenuFragment)

        val navBackStackEntry = findNavController().getBackStackEntry(R.id.MainMenuFragment)

        navBackStackEntry.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                p?.let { startGame(it) }
                Log.d("GameFragment", "dialog destroyed")
            }
        })
        return presenter
    }

    private fun startGame(presenter: Presenter) {
        binding.groupGameButtons.visibility = View.VISIBLE
        presenter.resumeGame()
    }

    private fun setButtons(presenter: Presenter) {
        binding.topButton.setOnClickListener {
            presenter.changeDirection(Presenter.DIR_TOP)
            click(it)
        }
        binding.rightButton.setOnClickListener {
            presenter.changeDirection(Presenter.DIR_RIGHT)
            click(it)
        }
        binding.bottomButton.setOnClickListener {
            presenter.changeDirection(Presenter.DIR_BOTTOM)
            click(it)
        }
        binding.leftButton.setOnClickListener {
            presenter.changeDirection(Presenter.DIR_LEFT)
            click(it)
        }
        binding.pause.setOnClickListener {
            if (!presenter.isPaused()) {
                //(it as AppCompatImageButton).setImageResource(R.drawable.ic_baseline_play_arrow_24)
                presenter.pauseGame()
                findNavController().navigate(R.id.action_GameFragment_to_MainMenuFragment)
                binding.groupGameButtons.visibility = View.INVISIBLE
                val navBackStackEntry = findNavController().getBackStackEntry(R.id.MainMenuFragment)

                navBackStackEntry.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        p?.let { startGame(it) }
                        Log.d("GameFragment", "dialog destroyed")
                    }
                })
            } else {
                //(it as AppCompatImageButton).setImageResource(R.drawable.ic_baseline_pause_24)
                presenter.resumeGame()
            }
            it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            it.startAnimation(AnimationUtils.loadAnimation(it.context, android.R.anim.fade_out))
        }
        binding.speedButton.setOnClickListener {
            binding.surfaceView.thread?.changeTicks()
            click(it)
        }
    }

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
        p?.let { startGame(it) }
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