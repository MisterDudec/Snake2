package com.example.snake2.ui.gameover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.snake2.activity.MainActivity
import com.example.snake2.R
import com.example.snake2.databinding.FragmentGameBinding
import com.example.snake2.databinding.FragmentGameOverBinding

class GameOverFragment : DialogFragment() {

    private var _binding: FragmentGameOverBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = GameOverFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        (activity as MainActivity).hideSystemBars()

        setStyle(
            STYLE_NORMAL,
            R.style.FullScreenDialogStyle
        )

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameOverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.restartGame.setOnClickListener {
            findNavController().navigate(R.id.action_GameOverFragment_to_GameFragment)
        }

        binding.buttonSettings.setOnClickListener {
            //findNavController().navigate(R.id.action_GameOverFragment_to_SettingFragment)
            findNavController().navigate(R.id.action_GameOverFragment_to_SettingsDialogFragment)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}