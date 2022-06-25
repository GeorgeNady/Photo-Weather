package com.robusta.image_converter.repo

import android.graphics.Bitmap
import android.widget.ImageView


internal interface ImageConverterRepo {
    /**
     * # Bitmap => ByteArray
     */
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray

    /**
     * # ByteArray => Bitmap
     */
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap

    /**
     * # ImageView Helpers
     */
    fun setBitmapToImageVIew(imageView: ImageView, bitmap: Bitmap)

    fun getBitmapFromImageVIew(imageView: ImageView): Bitmap

}