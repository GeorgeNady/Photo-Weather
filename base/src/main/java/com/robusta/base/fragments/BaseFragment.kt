package com.robusta.base.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import com.robusta.base.R
import java.io.File
import java.io.IOException


abstract class BaseFragment<T : ViewDataBinding?> : Fragment() {

    abstract val TAG: String
    private var contentId = 0
    var bundle: Bundle? = null
    var a: Activity? = null
    var binding: T? = null

    @Deprecated("Deprecated in Java")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (contentId == 0) {
            bundle = arguments
            contentId = ActivityFragmentAnnoationManager.check(this)
            a = activity
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, contentId, container, false)
        }
        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
        setListener()
    }

    protected abstract fun initialization() // TODO : add viewModel declaration
    protected abstract fun setListener() // TODO : Logic here

    val navOptions = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// {NAVIGATION} //////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    inline fun <reified A : Activity> Activity.startActivity(replace: Boolean? = true) {
        startActivity(Intent(this, A::class.java))
        replace?.let { finish() }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////// {DIALOG} ////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun sharePost(shareMessage: String?) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "PhotoWeather")
                putExtra(Intent.EXTRA_TEXT, shareMessage)
            }
            startActivity(Intent.createChooser(shareIntent, "Select one"))
        } catch (e: Exception) {
            Log.d(TAG, "onClick: ${e.message}")
        }
    }


    open fun openImage(path: String?) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.parse(path), "image/*")
        startActivity(intent)
    }

    open fun getImageRotation(imageFile: File): Int {
        var exif: ExifInterface? = null
        var exifRotation = 0
        try {
            exif = ExifInterface(imageFile.path)
            exifRotation =
                exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return if (exif == null) 0 else exifToDegrees(exifRotation)
    }

    open fun exifToDegrees(rotation: Int): Int {
        if (rotation == ExifInterface.ORIENTATION_ROTATE_90) return 90 else if (rotation == ExifInterface.ORIENTATION_ROTATE_180) return 180 else if (rotation == ExifInterface.ORIENTATION_ROTATE_270) return 270
        return 0
    }

    open fun getBitmapRotatedByDegree(bitmap: Bitmap, rotationDegree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.preRotate(rotationDegree.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

}