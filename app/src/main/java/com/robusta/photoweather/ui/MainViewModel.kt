package com.robusta.photoweather.ui

import android.app.Activity
import android.graphics.Bitmap
import android.location.Location
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robusta.photoweather.models.response.CurrentWeatherResponse
import com.robusta.photoweather.repository.MainRepo
import com.robusta.photoweather.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepo: MainRepo
) : ViewModel() {

    // private
    private val _response = MutableLiveData<Resource<CurrentWeatherResponse>>()

    // public
    val response get() = _response
    val location = MutableLiveData<Location>()

    fun getCurrentWeather(
        lon: Double,
        lat: Double,
        apiKey: String,
        units: String?,
        lang: String?
    ) = viewModelScope.launch {
        _response.value = Resource.loading()
        try {
            _response.value = mainRepo.getCurrentWeather(lon, lat, apiKey, units, lang)
        } catch (e: Exception) {
            _response.value = e.localizedMessage?.let { Resource.failed(it) }
        }
    }

    fun Activity.takeScreenshot(view: View): File? {
        val date = Date()
        try {
            // Initialising the directory of storage
            val dirpath: String = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()
            val file = File(dirpath)
            if (!file.exists()) {
                val mkdir = file.mkdir()
            }
            // File name : keeping file name unique using data time.
            val path = dirpath + "/" + date.time + ".jpeg"
            view.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
            val imageurl = File(path)
            val outputStream = FileOutputStream(imageurl)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            outputStream.flush()
            outputStream.close()
            Timber.d("takeScreenshot Path: $imageurl")
            Toast.makeText(this@takeScreenshot.baseContext, "" + imageurl, Toast.LENGTH_LONG).show()
            return imageurl
        } catch (io: FileNotFoundException) {
            io.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


}