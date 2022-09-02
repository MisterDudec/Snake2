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
    private val activity: MainActivity): SurfaceHolder.Callback {

    override fun surfaceCreated(holder: SurfaceHolder) {
        //Log.d("threads", "surfaceCreated: ${Looper.myLooper()}")

        viewModel.setDimensions(surfaceView.width, surfaceView.height)

        viewModel.setObserver(surfaceView)

        viewModel.liveData.observe(activity) {
            //Log.d("observe", "onViewCreated looper: ${Looper.myLooper()}")
            CoroutineScope(Dispatchers.Unconfined).launch {
                surfaceView.drawFrame(it)
            }
        }
        viewModel.startGame()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        viewModel.setDimensions(surfaceView.width, surfaceView.height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }
}