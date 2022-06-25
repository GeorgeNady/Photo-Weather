package com.robusta.photoweather.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robusta.photoweather.models.response.CurrentWeatherResponse
import com.robusta.photoweather.repository.MainRepo
import com.robusta.photoweather.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepo: MainRepo
) : ViewModel() {

    private val _response = MutableLiveData<Resource<CurrentWeatherResponse>>()
    val response get() = _response

    fun getCurrentWeather(
        lon: Long,
        lat: Long,
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

}