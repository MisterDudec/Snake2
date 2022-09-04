package com.example.snake2.surfaceview

import android.view.SurfaceHolder
import com.example.snake2.activity.GameViewModel
import com.example.snake2.activity.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SurfaceHolderCallback(
    private val viewModel: GameViewModel,
    private val surfaceView: GameSurfaceView,
    private val activity: MainActivity
    ): SurfaceHolder.Callback {

    override fun surfaceCreated(holder: SurfaceHolder) {
        viewModel.setDimensions(surfaceView.width, surfaceView.height)

        //viewModel.registerViewModelObserver(surfaceView)

        viewModel.liveData.observe(activity) {
            CoroutineScope(Dispatchers.Unconfined).launch {
                surfaceView.drawFrameRect(it)
            }
        }

        viewModel.startThreads()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        //viewModel.unregisterViewModelObserver()
        viewModel.setDimensions(surfaceView.width, surfaceView.height)
        //viewModel.registerViewModelObserver(surfaceView)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        //viewModel.joinThreads()
        //viewModel.unregisterViewModelObserver()
    }
}