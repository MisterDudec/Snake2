package com.example.snake2.ui.gameover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.snake2.R
import com.example.snake2.databinding.FragmentGameOverBinding
import com.example.snake2.activity.GameViewModel

class GameOverFragment : Fragment() {

    private var _binding: FragmentGameOverBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        //(activity as MainActivity).hideSystemBars()

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameOverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.restartGame.setOnClickListener {
            viewModel.restartGame()
            findNavController().navigate(R.id.action_GameOverFragment_to_GameFragment)
        }

        binding.buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_GameOverFragment_to_PreferenceContainerFragment)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}