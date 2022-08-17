package com.example.snake2.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.snake2.R
import com.example.snake2.databinding.FragmentGameBinding
import com.example.snake2.opeGL.OpenGLRenderer


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

@SuppressLint("NewApi")
class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private var renderer: OpenGLRenderer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        /*binding.surfaceView.*/
    }

    /*private fun startOpenGL(view: View) {
        binding.surfaceView.setEGLContextClientVersion(2)
        renderer = OpenGLRenderer(view.context)
        binding.surfaceView.setRenderer(renderer)
    }*/

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

    /*private fun supportES2(): Boolean {
        val activityManager = getSystemService<Any>(Context.ACTIVITY_SERVICE) as ActivityManager?
        val configurationInfo: ConfigurationInfo = activityManager!!.deviceConfigurationInfo
        return configurationInfo.reqGlEsVersion >= 0x20000
    }*/
}