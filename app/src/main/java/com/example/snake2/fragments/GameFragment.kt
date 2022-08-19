package com.example.snake2.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.snake2.Presenter
import com.example.snake2.R
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

        /*binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }*/

        //val Button[
        val presenter = Presenter()
        binding.surfaceView.startGame(presenter)

        binding.topButton.setOnClickListener { presenter.changeDirection(Presenter.DIR_TOP) }
        binding.rightButton.setOnClickListener { presenter.changeDirection(Presenter.DIR_RIGHT) }
        binding.bottomButton.setOnClickListener { presenter.changeDirection(Presenter.DIR_BOTTOM) }
        binding.leftButton.setOnClickListener { presenter.changeDirection(Presenter.DIR_LEFT) }
    }

    override fun onDestroyView() {
        binding.surfaceView.surfaceDestroyed(binding.surfaceView.holder)
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        binding.surfaceView.surfaceDestroyed(binding.surfaceView.holder)
        super.onPause()
        //binding.surfaceView.
    }

    override fun onResume() {
        super.onResume()
        //binding.surfaceView.onResume()
    }


}