package com.robusta.photoweather.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.robusta.base.fragments.ActivityFragmentAnnoation
import com.robusta.base.fragments.BaseFragment
import com.robusta.photoweather.databinding.FragmentAddWeatherBinding
import com.robusta.photoweather.ui.MainActivity
import com.robusta.photoweather.utilities.Constants.ADD_WEATHER_FRAG
import com.robusta.photoweather.utilities.Constants.API_KEY
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


@ActivityFragmentAnnoation(ADD_WEATHER_FRAG)
class AddWeatherFragment : BaseFragment<FragmentAddWeatherBinding>(), LocationListener {

    override val TAG: String get() = this::class.java.simpleName
    private val mainViewModel by lazy { (activity as MainActivity).mainViewModel }

    private val args by navArgs<AddWeatherFragmentArgs>()
    private val uri by lazy { args.uri }

    private var isApiCalled = false
    private val locationManager by lazy { requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager }

    override fun onLocationChanged(location: Location) {
        mainViewModel.location.value = location
    }


    private val permissionstorage = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun initViewModel() {
        binding?.apply {
            ivCapturedPicture.setImageURI(uri)
        }
    }

    override fun setListener() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)

        binding?.apply {
            mainViewModel.location.observe(this@AddWeatherFragment) { location ->
                Timber.d("""
                    accuracy: ${location.accuracy}
                    latitude: ${location.latitude}
                    longitude: ${location.longitude}
                """.trimIndent())

                /*if (!isApiCalled) {
                    Timber.d("locationChangeListener #3 >>> isApiCalled $isApiCalled")
                    if (it.accuracy <= 50f) {
                        mainViewModel.getCurrentWeather(it.longitude, it.latitude, API_KEY, null, null)
                        isApiCalled = true
                        Timber.d("Api was Called")
                    }
                }*/
            }
            mainViewModel.response.observe(viewLifecycleOwner) { res ->
                res.handler(
                    mLoading = { loading() },
                    mError = { finishLoading(it) },
                    mFailed = { finishLoading(it) }
                ) {
                    finishLoading()
                    bWeatherInfo = """
                        City Name: ${it.name}
                        weather: ${it.weather[0].main}
                        description: ${it.weather[0].description}
                        Temperature: ${it.main.temp}
                        Feel like: ${it.main.feels_like}
                        Humidity: ${it.main.humidity}
                        Wind Speed: ${it.wind.speed}
                        Wind Degree: ${it.wind.deg}
                    """.trimIndent()
                }
            }

            btnShare.setOnClickListener {
                mainViewModel.apply {
                    requireActivity().takeScreenshot(cardView)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // remove location listener
        locationManager.removeUpdates(this)
    }

    private fun FragmentAddWeatherBinding.loading() {
        progressBar.visibility = View.VISIBLE
        tvWeatherInfo.visibility = View.GONE
    }

    private fun FragmentAddWeatherBinding.finishLoading(message: String? = null) {
        progressBar.visibility = View.GONE
        tvWeatherInfo.visibility = View.VISIBLE
        message?.let {
            tvWeatherInfo.text = it
        }

    }

    // check weather storage permission is given or not
    /*fun checkpermissions(activity: Activity?) {
        val permissions = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        // If storage permission is not given then request for External Storage Permission
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissionstorage, REQUEST_EXTERNAL_STORAGE)
        }
    }*/

}