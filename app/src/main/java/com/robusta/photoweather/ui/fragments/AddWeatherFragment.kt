package com.robusta.photoweather.ui.fragments

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.navArgs
import com.facebook.CallbackManager
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareButton
import com.robusta.base.fragments.ActivityFragmentAnnoation
import com.robusta.base.fragments.BaseFragment
import com.robusta.image_converter.ImageConverter.bitmapToByteArray
import com.robusta.image_converter.ImageConverter.getBitmapFromImageVIew
import com.robusta.photoweather.databinding.FragmentAddWeatherBinding
import com.robusta.photoweather.models.domain.PhotoWeather
import com.robusta.photoweather.ui.MainActivity
import com.robusta.photoweather.utilities.Constants.ADD_WEATHER_FRAG
import com.robusta.photoweather.utilities.Constants.API_KEY
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
@ActivityFragmentAnnoation(ADD_WEATHER_FRAG)
class AddWeatherFragment : BaseFragment<FragmentAddWeatherBinding>(), LocationListener {

    override val TAG: String get() = this::class.java.simpleName
    private val mainViewModel by lazy { (activity as MainActivity).mainViewModel }

    @Inject
    lateinit var callbackManager: CallbackManager

    private val args by navArgs<AddWeatherFragmentArgs>()
    private val uri by lazy { args.uri }

    private var isApiCalled = false
    private val locationManager by lazy { requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager }

    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onLocationChanged(location: Location) {
        mainViewModel.location.value = location.also {
            Timber.d(
                """
                accuracy: ${location.accuracy}
                latitude: ${location.latitude}
                longitude: ${location.longitude}
            """
            )
        }
    }

    override fun initialization() {
        binding?.apply {
            ivCapturedPicture.setImageURI(uri)
        }
    }

    override fun setListener() {
        binding?.apply {
            mainViewModel.location.observe(this@AddWeatherFragment) { location ->

                if (!isApiCalled) {
                    if (location.accuracy <= 50f) {
                        mainViewModel.getCurrentWeather(
                            location.latitude,
                            location.longitude,
                            API_KEY,
                            null,
                            null
                        )
                        isApiCalled = true
                        Timber.d("Api was Called")
                    }
                }
            }

            btnShareFacebook.setOnClickListener {
                mainViewModel.apply {
                    val thumbnailBitmap = takeViewSnapshot(cardView)
                    Timber.d("btnShareFacebook >>> takeViewSnapshot from card view ${thumbnailBitmap.byteCount}")
                    (it as ShareButton).shareContent = facebookShareHandler(thumbnailBitmap)
                }
            }

            mainViewModel.response.observe(viewLifecycleOwner) { res ->
                res.handler(
                    mLoading = { loading() },
                    mError = { finishLoading(it) },
                    mFailed = { finishLoading(it) }
                ) {
                    finishLoading()
                    btnShareFacebook.isEnabled = true
                    bWeatherInfo = """
                        City Name: ${it.name}
                        Weather: ${it.weather[0].main}
                        Description: ${it.weather[0].description}
                        Temperature: ${it.main.temp}
                        Feel like: ${it.main.feels_like}
                        Humidity: ${it.main.humidity}
                        Wind Speed: ${it.wind.speed}
                        Wind Degree: ${it.wind.deg}
                    """.trimIndent()

                    // TODO: Save record into a database

                    // Image
                    val imageBitmap = getBitmapFromImageVIew(ivCapturedPicture)
                    val imageByteArray = bitmapToByteArray(imageBitmap)
                    Log.d(TAG, "setListener: imageByteArray: $imageByteArray")

                    // thumbnail
                    val thumbnailBitmap = mainViewModel.takeViewSnapshot(cardView)
                    val thumbnailByteArray = bitmapToByteArray(thumbnailBitmap)
                    Log.d(TAG, "setListener: thumbnailByteArray: $thumbnailByteArray")

                    // create entity object
                    val photoWeather = PhotoWeather(
                        name = it.name,
                        temp = it.main.temp,
                        feelsLike = it.main.feels_like,
                        humidity = it.main.humidity,
                        windSpeed = it.wind.speed,
                        windDeg = it.wind.deg,
                        image = imageByteArray,
                        thumbnail = thumbnailByteArray,
                    )

                    // save to the database
                    Toast.makeText(requireContext(), photoWeather.toString(), Toast.LENGTH_LONG).show()
                    Log.d(TAG, "setListener: ${photoWeather.toString()}")
                   // mainViewModel.createPhotoHistory(photoWeather)

                }
            }

            mainViewModel.createHistoryPhoto.observe(viewLifecycleOwner) { res ->
                res.handler(
                    mLoading = { loadingLayout.show() },
                    mError = { loadingLayout.gone(it) },
                    mFailed = { loadingLayout.gone(it) }
                ) {
                    loadingLayout.gone("Photo Saved Successfully in the History")
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        locationPermissionsRequest.launch(requiredPermissions)
    }

    override fun onPause() {
        super.onPause()
        // remove location listener
        locationManager.removeUpdates(this)
    }

    private fun facebookShareHandler(screenShoot: Bitmap): SharePhotoContent {
        val sharePhotoContent: SharePhotoContent
        mainViewModel.apply {
            val sharePhoto: SharePhoto = SharePhoto.Builder()
                .setBitmap(screenShoot)
                .build()
            sharePhotoContent = SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .build()
        }
        return sharePhotoContent
    }

    private fun FragmentAddWeatherBinding.loading() {
        progressBar.show()
        tvWeatherInfo.gone()
    }

    private fun FragmentAddWeatherBinding.finishLoading(message: String? = null) {
        progressBar.gone()
        tvWeatherInfo.show()
        message?.let {
            tvWeatherInfo.text = it
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////// ACTIVITY RESULTS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private val locationPermissionsRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions: Map<String, Boolean> ->

        val results = mutableListOf<Boolean>()
        permissions.forEach {
            Timber.d("${it.key}, ${it.value}")
            results.add(permissions.getOrDefault(it.key, false))
        }

        if (results.contains(false)) {
            // open dialog to go to setting manual
            Timber.d("result contains false")
        } else {
            Timber.d("Needed permissions granted")
            // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0f, this)
        }

    }
}