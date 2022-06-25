package com.robusta.photoweather.ui.fragments

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.navArgs
import com.facebook.CallbackManager
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareButton
import com.facebook.share.widget.ShareDialog
import com.robusta.base.fragments.ActivityFragmentAnnoation
import com.robusta.base.fragments.BaseFragment
import com.robusta.photoweather.databinding.FragmentAddWeatherBinding
import com.robusta.photoweather.ui.MainActivity
import com.robusta.photoweather.utilities.Constants.ADD_WEATHER_FRAG
import com.robusta.photoweather.utilities.Constants.API_KEY
import timber.log.Timber
import javax.inject.Inject


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

    private val shareDialog by lazy { ShareDialog((activity as MainActivity)) }

    override fun onLocationChanged(location: Location) {
        mainViewModel.location.value = location
        Timber.d("""
            accuracy: ${location.accuracy}
            latitude: ${location.latitude}
            longitude: ${location.longitude}
        """.trimIndent())
    }

    override fun initialization() {
        binding?.apply {
            ivCapturedPicture.setImageURI(uri)
        }
    }

    override fun setListener() {
        locationPermissionsRequest.launch(requiredPermissions)

        binding?.apply {
            mainViewModel.location.observe(this@AddWeatherFragment) { location ->
                if (!isApiCalled) {
                    if (location.accuracy <= 50f) {
                        mainViewModel.getCurrentWeather(location.longitude, location.latitude, API_KEY, null, null)
                        isApiCalled = true
                        Timber.d("Api was Called")
                    }
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
                }
            }

            btnFacebookLoing

            btnShareFacebook.setOnClickListener {
                mainViewModel.apply {
                    val screenShoot = requireActivity().takeViewSnapshot(cardView)
                    val sharePhoto: SharePhoto = SharePhoto.Builder()
                        .setBitmap(screenShoot)
                        .build()
                    val sharePhotoContent: SharePhotoContent = SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .build()

                    shareDialog.show(sharePhotoContent)
                    //(it as ShareButton).shareContent = sharePhotoContent
                }

            }

        }
    }

    override fun onPause() {
        super.onPause()
        // remove location listener
        locationManager.removeUpdates(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0f, this)
        }

    }
}