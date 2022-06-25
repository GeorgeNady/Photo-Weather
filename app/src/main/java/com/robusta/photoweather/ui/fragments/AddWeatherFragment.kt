package com.robusta.photoweather.ui.fragments

import android.view.View
import androidx.navigation.fragment.navArgs
import com.robusta.base.fragments.ActivityFragmentAnnoation
import com.robusta.base.fragments.BaseFragment
import com.robusta.photoweather.databinding.FragmentAddWeatherBinding
import com.robusta.photoweather.ui.MainActivity
import com.robusta.photoweather.utilities.Constants.ADD_WEATHER_FRAG
import com.robusta.photoweather.utilities.Constants.API_KEY


@ActivityFragmentAnnoation(ADD_WEATHER_FRAG)
class AddWeatherFragment : BaseFragment<FragmentAddWeatherBinding>() {

    override val TAG: String get() = this::class.java.simpleName
    private val mainViewModel by lazy { (activity as MainActivity).mainViewModel }

    private val args by navArgs<AddWeatherFragmentArgs>()
    private val uri by lazy { args.uri }

    override fun initViewModel() {
        binding?.apply {
            ivCapturedPicture.setImageURI(uri)
        }
    }

    override fun setListener() {
        binding?.apply {
            mainViewModel.getCurrentWeather(12.265165.toLong(), 21.5615651.toLong(), API_KEY,null,null)

            mainViewModel.response.observe(viewLifecycleOwner) { res ->
                res.handler(
                    mLoading = {loading()},
                    mError = {finishLoading(it)},
                    mFailed = {finishLoading(it)}
                ) {
                    finishLoading(it.toString())
                }
            }
        }
    }

    private fun FragmentAddWeatherBinding.loading() {
        progressBar.visibility = View.VISIBLE
        tvWeatherInfo.visibility = View.GONE
    }

    private fun FragmentAddWeatherBinding.finishLoading(message: String) {
        progressBar.visibility = View.GONE
        tvWeatherInfo.visibility = View.VISIBLE
        tvWeatherInfo.text = message
    }

}