package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import com.example.background.R

private const val TAG = "BlurWorker"

class BlurWorker(ctx : Context , params: WorkerParameters) : Worker(ctx,params) {
    override fun doWork(): Result {
        val appContext = applicationContext
        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        makeStatusNotification("Bluring image", appContext)
        sleep()
        try {
            //val picture = BitmapFactory.decodeResource(appContext.resources, R.drawable.android_cupcake)

            if(TextUtils.isEmpty(resourceUri)){
                Log.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val picture = BitmapFactory.decodeStream(
                appContext.contentResolver.openInputStream(Uri.parse(resourceUri)))
            val bitmap = blurBitmap(picture, appContext)
            val outputUri = writeBitmapToFile(appContext, bitmap)
            makeStatusNotification("Output is $outputUri",appContext)
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
            return Result.success(outputData)

        }catch ( e: Throwable){
            Log.e(TAG,"Error applying blur")
            makeStatusNotification("Error applying blur",appContext)
            return Result.failure()
        }


    }
}