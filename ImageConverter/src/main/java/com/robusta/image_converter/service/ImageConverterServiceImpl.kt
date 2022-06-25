package com.robusta.image_converter.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.robusta.image_converter.repo.ImageConverterRepo
import java.io.ByteArrayOutputStream

internal class ImageConverterServiceImpl : ImageConverterRepo {

    override fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        return stream.toByteArray()
    }

    override fun byteArrayToBitmap(byteArray: ByteArray): Bitmap =
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    override fun setBitmapToImageVIew(imageView: ImageView, bitmap: Bitmap) =
        imageView.setImageBitmap(
            Bitmap.createScaledBitmap(
                bitmap,
                imageView.width,
                imageView.height,
                false
            )
        )

    override fun getBitmapFromImageVIew(imageView: ImageView): Bitmap =
        (imageView.drawable as BitmapDrawable).bitmap
}