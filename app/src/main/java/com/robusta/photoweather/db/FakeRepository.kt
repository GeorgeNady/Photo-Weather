package com.robusta.photoweather.db

import com.robusta.photoweather.models.domain.PhotoWeather

object FakeRepository {

    /**
     * for testing adapter
     */
    fun fakeHistoryPhotos(): List<PhotoWeather> {
        val photoWeather1 = PhotoWeather(
            1,
            1L,
            "Cairo",
            20.0,
            19.0,
            0,
            15.0,
            0,
            byteArrayOf(0x00, 0x00, 0x00, 0x00),
            byteArrayOf(0x00, 0x00, 0x00, 0x00)
        )
        val photoWeather2 = PhotoWeather(
            2,
            1L,
            "Cairo",
            20.0,
            19.0,
            0,
            15.0,
            0,
            byteArrayOf(0x00, 0x00, 0x00, 0x00),
            byteArrayOf(0x00, 0x00, 0x00, 0x00)
        )
        val photoWeather3 = PhotoWeather(
            3,
            1L,
            "Cairo",
            20.0,
            19.0,
            0,
            15.0,
            0,
            byteArrayOf(0x00, 0x00, 0x00, 0x00),
            byteArrayOf(0x00, 0x00, 0x00, 0x00)
        )
        val photoWeather4 = PhotoWeather(
            4,
            1L,
            "Cairo",
            20.0,
            19.0,
            0,
            15.0,
            0,
            byteArrayOf(0x00, 0x00, 0x00, 0x00),
            byteArrayOf(0x00, 0x00, 0x00, 0x00)
        )
        val photoWeather5 = PhotoWeather(
            5,
            1L,
            "Cairo",
            20.0,
            19.0,
            0,
            15.0,
            0,
            byteArrayOf(0x00, 0x00, 0x00, 0x00),
            byteArrayOf(0x00, 0x00, 0x00, 0x00)
        )
        return listOf(photoWeather1, photoWeather2, photoWeather3, photoWeather4, photoWeather5)
    }
}