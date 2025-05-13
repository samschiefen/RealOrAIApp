package com.example.realorai.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun assetFilePath(context: Context, assetName: String): String {
    val file = File(context.filesDir, assetName)
    if (file.exists() && file.length() > 0) {
        return file.absolutePath
    }

    context.assets.open(assetName).use { inputStream ->
        FileOutputStream(file).use { outputStream ->
            val buffer = ByteArray(4 * 1024)
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()
        }
    }
    return file.absolutePath
}


fun runModelOnImage(context: Context, bitmap: Bitmap): String {
    // Step 1: Resize shorter side to 360 (preserve aspect ratio)
    val width = bitmap.width
    val height = bitmap.height
    val scaleFactor = 320f / min(width, height)

    val resizedWidth = (width * scaleFactor).toInt()
    val resizedHeight = (height * scaleFactor).toInt()

    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, true)

    // Step 2: Center crop to 320x320 to match PyTorch CenterCrop
    val xOffset = (resizedBitmap.width - 320) / 2
    val yOffset = (resizedBitmap.height - 320) / 2
    val croppedBitmap = Bitmap.createBitmap(resizedBitmap, xOffset, yOffset, 320, 320)

    // Step 3: Load the model
    val module = Module.load(assetFilePath(context, "test2_model.pt"))

    // Step 4: Convert bitmap to Tensor
    // Use (0.5, 0.5, 0.5) for mean and std if you normalized with that in PyTorch
    val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
        croppedBitmap,
        floatArrayOf(0.5f, 0.5f, 0.5f), // match Normalize mean
        floatArrayOf(0.5f, 0.5f, 0.5f)  // match Normalize std
    )

    // Step 5: Run the model
    val outputTensor = module.forward(IValue.from(inputTensor)).toTensor()
    val scores = outputTensor.dataAsFloatArray

    // Step 6: Interpret result
    return if (scores[0] > 0.5f) "AI-Generated" else "Real Image"
}
