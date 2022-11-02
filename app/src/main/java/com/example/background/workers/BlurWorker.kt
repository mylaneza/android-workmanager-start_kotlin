package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.R

private const val TAG = "BlurWorker"

class BlurWorker(ctx : Context , params: WorkerParameters) : Worker(ctx,params) {
    override fun doWork(): Result {
        val appContext = applicationContext
        makeStatusNotification("Bluring image", appContext)
        try {
            val picture =
                BitmapFactory.decodeResource(appContext.resources, R.drawable.android_cupcake)
            val bitmap = blurBitmap(picture, appContext)
            val uri = writeBitmapToFile(appContext, bitmap)
            makeStatusNotification("Output is $uri",appContext)
        }catch ( e: Throwable){
            Log.e(TAG,"Error applying blur")
            makeStatusNotification("Error applying blur",appContext)
            return Result.failure()
        }
        return Result.success()

    }
}