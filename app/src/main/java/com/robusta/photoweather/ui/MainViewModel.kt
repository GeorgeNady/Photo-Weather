package com.robusta.photoweather.ui

import android.R.attr.label
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Bitmap
import android.location.Location
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.CallbackManager
import com.robusta.photoweather.models.domain.PhotoWeather
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
import java.util.*
import javax.inject.Inject

typealias WeatherResponseMLD = MutableLiveData<Resource<CurrentWeatherResponse>>
typealias WeatherResponseLD = MutableLiveData<Resource<CurrentWeatherResponse>>

typealias PhotosMLD = MutableLiveData<Resource<MutableList<PhotoWeather>>>
typealias PhotosLD = LiveData<Resource<MutableList<PhotoWeather>>>

typealias PhotoMLD = MutableLiveData<Resource<PhotoWeather>>
typealias PhotoLD = LiveData<Resource<PhotoWeather>>

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepo: MainRepo,
) : ViewModel() {

    // private
    private val _response = WeatherResponseMLD()
    private val _historyPhoto = PhotosMLD()
    private val _createHistoryPhoto = PhotoMLD()

    // public
    val response : WeatherResponseLD get() = _response
    val historyPhoto : PhotosLD get() = _historyPhoto
    val createHistoryPhoto : PhotoLD get() = _createHistoryPhoto
    val location = MutableLiveData<Location>()

    //////////////////////////////////////////////////////////////////////////////////////// NETWORK
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

    /////////////////////////////////////////////////////////////////////////////////////// DATABASE
    fun getAllHistory() {
        _historyPhoto.value = Resource.loading()
        try {
            _historyPhoto.value = Resource.success(mainRepo.getPhotosWeather())
        } catch (e: Exception) {
            _historyPhoto.value = Resource.failed(e.message.toString())

        }
    }

    fun createPhotoHistory(photoWeather: PhotoWeather) = viewModelScope.launch {
        _createHistoryPhoto.value = Resource.loading()
        try {
            val isCreated = mainRepo.insertPhotoWeather(photoWeather)
            isCreated?.let {
                if (it == 1L) {
                    _createHistoryPhoto.value = Resource.success(photoWeather)
                    _historyPhoto.value?.data?.add(photoWeather)
                    val newList =  _historyPhoto.value?.data!!
                    _historyPhoto.postValue(Resource.success(newList))
                }
            }
        } catch (e: Exception) {
            _createHistoryPhoto.value = Resource.failed(e.message.toString())
        }
    }

    /////////////////////////////////////////////////////////////////////////////// HELPER FUNCTIONS
    fun takeViewSnapshot(view: View): Bitmap {
        var bitmap: Bitmap? = null
        try {
            view.isDrawingCacheEnabled = true
            bitmap = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
        } catch (e:Exception) {
            Timber.e(e.message)
        }
        return bitmap!!
    }

}