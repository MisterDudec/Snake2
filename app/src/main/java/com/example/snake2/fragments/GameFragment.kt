package com.example.snake2.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.example.snake2.Presenter
import com.example.snake2.R
import com.example.snake2.data.GameFieldData
import com.example.snake2.databinding.FragmentGameBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

@SuppressLint("NewApi")
class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backgroundColor = view.context.getColor(R.color.background_surface_view)
        val snakeColor = view.context.getColor(R.color.snake)
        val appleColor = view.context.getColor(R.color.apple)

        view.post { //post is necessary to wait till drawing phase of view and get actual dimensions
            val presenter = Presenter(GameFieldData(view.measuredWidth, view.measuredHeight), backgroundColor, snakeColor, appleColor)
            binding.surfaceView.startGame(presenter)
            setButtons(presenter)
        }

    }

    private fun setButtons(presenter: Presenter) {
        binding.topButton.setOnClickListener {
            presenter.changeDirection(Presenter.DIR_TOP)
            it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            it.startAnimation(AnimationUtils.loadAnimation(it.context, android.R.anim.fade_in))
        }
        binding.rightButton.setOnClickListener {
            presenter.changeDirection(Presenter.DIR_RIGHT)
            it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            it.startAnimation(AnimationUtils.loadAnimation(it.context, android.R.anim.fade_in))
        }
        binding.bottomButton.setOnClickListener {
            presenter.changeDirection(Presenter.DIR_BOTTOM)
            it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            it.startAnimation(AnimationUtils.loadAnimation(it.context, android.R.anim.fade_in))
        }
        binding.leftButton.setOnClickListener {
            presenter.changeDirection(Presenter.DIR_LEFT)
            it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            it.startAnimation(AnimationUtils.loadAnimation(it.context, android.R.anim.fade_in))
        }
        binding.pause.setOnClickListener {
            if (it is AppCompatImageButton ) {
                if (!presenter.pause) it.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                else it.setImageResource(R.drawable.ic_baseline_pause_24)
            }
            presenter.pauseResumeGame()
            it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            it.startAnimation(AnimationUtils.loadAnimation(it.context, android.R.anim.fade_in))
        }
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