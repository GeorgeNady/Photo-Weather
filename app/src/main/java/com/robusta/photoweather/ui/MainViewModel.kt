package com.robusta.photoweather.ui

import android.graphics.Bitmap
import android.location.Location
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robusta.photoweather.models.domain.PhotoWeather
import com.robusta.photoweather.models.response.CurrentWeatherResponse
import com.robusta.photoweather.repository.MainRepo
import com.robusta.photoweather.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

typealias WeatherResponseMLD = MutableLiveData<Resource<CurrentWeatherResponse>>
typealias WeatherResponseLD = MutableLiveData<Resource<CurrentWeatherResponse>>

typealias PhotosMLD = MutableLiveData<Resource<MutableList<PhotoWeather>>>
typealias PhotosLD = LiveData<Resource<MutableList<PhotoWeather>>>

typealias PhotoMLD = MutableLiveData<Resource<PhotoWeather>>
typealias PhotoLD = LiveData<Resource<PhotoWeather>>

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepo: MainRepo
) : ViewModel() {

    // private
    private val _response = WeatherResponseMLD()
    private val _createHistoryPhoto = PhotoMLD()

    // public
    val location = MutableLiveData<Location>()
    val response: WeatherResponseLD get() = _response

    val createHistoryPhoto: PhotoLD get() = _createHistoryPhoto
    val historyPhoto = PhotosMLD()

    //////////////////////////////////////////////////////////////////////////////////////// NETWORK
    fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String?,
        lang: String?
    ) = viewModelScope.launch {
        _response.value = Resource.loading()
        try {
            _response.value = mainRepo.getCurrentWeather(lat, lon, apiKey, units, lang)
        } catch (e: Exception) {
            _response.value = e.localizedMessage?.let { Resource.failed(it) }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////// DATABASE
    fun getAllHistory() {
        historyPhoto.postValue(Resource.loading())
        Timber.d("getAllHistory() >>> loading fetching history from the database....")
        val photoWeatherHistoryLivedata = mainRepo.getPhotosWeather()
        try {
            photoWeatherHistoryLivedata.value?.let {
                Timber.d("getAllHistory() >>> trying to fetch history from the database")
                historyPhoto.postValue(Resource.success(it.toMutableList()))
            } ?: Timber.d("getAllHistory() >>> photoWeatherHistoryLivedata == null")

        } catch (e:Exception) {
            Timber.d("getAllHistory() >>> failed to fetch history from the database with 'Exception::${e.message}'")
            historyPhoto.postValue(Resource.failed(e.stackTraceToString()))
        }
    }

    fun createPhotoHistory(photoWeather: PhotoWeather) = viewModelScope.launch(Dispatchers.IO) {
        _createHistoryPhoto.postValue(Resource.loading())
        Timber.d("createPhotoHistory() >> loading")
        try {
            val isCreated = mainRepo.insertPhotoWeather(photoWeather)
            Timber.d("createPhotoHistory() >> isCreated: $isCreated")
            _createHistoryPhoto.postValue(Resource.success(photoWeather))
        } catch (e: Exception) {
            _createHistoryPhoto.postValue(Resource.failed(e.message.toString()))
            Timber.e(e.message)
        }
    }

    /////////////////////////////////////////////////////////////////////////////// HELPER FUNCTIONS
    fun takeViewSnapshot(view: View): Bitmap {
        var bitmap: Bitmap? = null
        try {
            view.isDrawingCacheEnabled = true
            bitmap = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
        } catch (e: Exception) {
            Timber.e(e.message)
        }
        return bitmap!!
    }

}