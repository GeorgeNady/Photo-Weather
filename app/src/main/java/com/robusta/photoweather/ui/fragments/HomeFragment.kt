package com.robusta.photoweather.ui.fragments

import android.Manifest.permission.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.robusta.base.fragments.ActivityFragmentAnnoation
import com.robusta.base.fragments.BaseFragment
import com.robusta.photoweather.R
import com.robusta.photoweather.adapter.PhotoWeatherAdapter
import com.robusta.photoweather.databinding.FragmentHomeBinding
import com.robusta.photoweather.ui.MainActivity
import com.robusta.photoweather.ui.dialogs.PickImageDialogFragment
import com.robusta.photoweather.utilities.Constants.FILE_PROVIDER
import com.robusta.photoweather.utilities.Constants.HOME_FRAG
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
@ActivityFragmentAnnoation(HOME_FRAG)
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val TAG: String get() = this::class.java.simpleName
    private val mainViewModel by lazy { (activity as MainActivity).mainViewModel }
    private val rvAdapter by lazy { PhotoWeatherAdapter() }

    private lateinit var dialog: PickImageDialogFragment

    private lateinit var mUri: Uri
    private lateinit var file: File
    private lateinit var mCurrentPhotoPath: String

    override fun initialization() {}

    override fun setListener() {
        binding?.apply {
            rvWeatherHistory.adapter = rvAdapter

            fabAddWeatherStatus.setOnClickListener {
                locationPermissionsRequest.launch(
                    arrayOf(
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,
                        CAMERA
                    )
                )
            }

            mainViewModel.historyPhoto.observe(viewLifecycleOwner) { dbRes ->
                dbRes.handler{
                    Timber.d("historyPhoto >>> Success: database count: ${dbRes.data?.size ?: 0}")
                    rvAdapter.submitList(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getAllHistory()
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////// RESULT HANDLER
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private val getCameraImage =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                Timber.d("image location $mUri")
                dialog.dismiss()
                val bundle = Bundle().also {
                    it.putParcelable("uri", mUri)
                }
                findNavController().navigate(R.id.addWeatherFragment, bundle, navOptions)
            } else Timber.e("takerPicture $success")
        }

    private val getGalleryImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { internalUri ->
                dialog.dismiss()
                val bundle = Bundle().also {
                    it.putParcelable("uri", internalUri)
                }
                findNavController().navigate(R.id.addWeatherFragment, bundle, navOptions)
            }
        }

    private val locationPermissionsRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions: Map<String, Boolean> ->

        val results = mutableListOf<Boolean>()
        permissions.forEach {
            Timber.d(it.key)
            results.add(permissions.getOrDefault(it.key, false))
        }

        if (results.contains(false)) {
            // open dialog to go to setting manual
        } else {
            dialog = PickImageDialogFragment {
                when (it) {
                    1 -> invokeCamera()
                    2 -> invokeImageGallery()
                }
            }
            dialog.show(requireActivity().supportFragmentManager, this::class.simpleName)
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////// IMAGE HANDLER
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun invokeCamera() {
        val file = createImageFile()
        try {
            mUri = FileProvider.getUriForFile(requireContext(), FILE_PROVIDER, file)
        } catch (e: Exception) {
            Timber.e("Error ${e.message}")
        }
        getCameraImage.launch(mUri)
    }

    private fun invokeImageGallery() {
        getGalleryImage.launch("image/*")
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(Date())
        val imageFileName = "photoweather_${timeStamp}"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(imageFileName, ".jpg", storageDir)

        // Save a file: path for use with ACTION_VIEW intents
        return file.also {
            mCurrentPhotoPath = it.absolutePath
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////// RECYCLER VIEW
    ////////////////////////////////////////////////////////////////////////////////////////////////

}