package com.example.snake2.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.snake2.databinding.FragmentMenuBinding
import androidx.navigation.fragment.findNavController
import com.example.snake2.R

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonStartGame.setOnClickListener {
            with (findNavController()) {
                if (currentDestination == findDestination(R.id.MenuFragment))
                    navigate(R.id.action_MenuFragment_to_GameFragment)
            }
        }

        binding.buttonSettings.setOnClickListener {
            with (findNavController()) {
                if (currentDestination == findDestination(R.id.MenuFragment))
                    navigate(R.id.action_MenuFragment_to_PreferenceContainerFragment)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

}