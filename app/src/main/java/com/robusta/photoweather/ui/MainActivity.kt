package com.robusta.photoweather.ui

import androidx.activity.viewModels
import com.robusta.base.BaseActivity
import com.robusta.photoweather.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate
) {

    override val TAG: String get() = this::class.java.simpleName
    val mainViewModel by viewModels<MainViewModel>()

    override fun setListener() {}

}