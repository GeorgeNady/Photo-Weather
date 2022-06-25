package com.robusta.image_converter

import android.graphics.Bitmap
import android.widget.ImageView
import com.robusta.image_converter.repo.ImageConverterRepo
import org.koin.java.KoinJavaComponent.inject

object ImageConverter {

    private val repo: ImageConverterRepo by inject(ImageConverterRepo::class.java)

    fun getBitmapFromImageVIew(imageView: ImageView): Bitmap =
        repo.getBitmapFromImageVIew(imageView)

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray = repo.bitmapToByteArray(bitmap)

    fun setBitmapToImageVIew(imageView: ImageView, bitmap: Bitmap) =
        repo.setBitmapToImageVIew(imageView, bitmap)

    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap =
        repo.byteArrayToBitmap(byteArray)

}