package com.robusta.photoweather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.robusta.image_converter.ImageConverter
import com.robusta.photoweather.databinding.ItemWeatherHistoryBinding
import com.robusta.photoweather.models.domain.PhotoWeather

class PhotoWeatherAdapter :
    ListAdapter<PhotoWeather, PhotoWeatherAdapter.PhotoWeatherViewHolder>(Differ) {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // info ------------------------------------------------------------------------------ Variables
    ////////////////////////////////////////////////////////////////////////////////////////////////
    object Differ : DiffUtil.ItemCallback<PhotoWeather>() {
        override fun areItemsTheSame(oldItem: PhotoWeather, newItem: PhotoWeather) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PhotoWeather, newItem: PhotoWeather) =
            oldItem == newItem
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // info ---------------------------------------------------------------------------- View Holder
    ////////////////////////////////////////////////////////////////////////////////////////////////
    inner class PhotoWeatherViewHolder(val binding: ItemWeatherHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // info ----------------------------------------------------- Recycler View Members to Implement
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun getItemCount() = currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoWeatherViewHolder {
        return PhotoWeatherViewHolder(
            ItemWeatherHistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoWeatherViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            setListener(item, position, holder)
        }
    }

    //Y//////////////////////////////////////////////////////////////////////////////////// Listener
    private fun ItemWeatherHistoryBinding.setListener(
        photoWeather: PhotoWeather,
        position: Int,
        holder: PhotoWeatherViewHolder
    ) {

        // TODO:
        // ivCapturedPicture.load(ImageConverter.byteArrayToBitmap(photoWeather.image!!))


        bWeatherInfo = """
                        City Name: ${photoWeather.name}
                        Temperature: ${photoWeather.temp}
                        Feel like: ${photoWeather.feelsLike}
                        Humidity: ${photoWeather.humidity}
                        Wind Speed: ${photoWeather.windSpeed}
                        Wind Degree: ${photoWeather.windDeg}
                    """.trimIndent()

        container.setOnClickListener { view ->
            onclickListener?.let { it(photoWeather, position, view, holder) }
        }

    }

    //Y///////////////////////////////////////////////////////////////////////////// Click Interface
    private var onclickListener: ((PhotoWeather, Int, View, PhotoWeatherViewHolder) -> Unit)? = null

    fun setOnItemClickListener(listener: (PhotoWeather, Int, View, PhotoWeatherViewHolder) -> Unit) {
        onclickListener = listener
    }

}

