package com.robusta.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

public abstract class BaseActivity<B : ViewBinding>(
    val bindingFactory: (LayoutInflater) -> B
) : AppCompatActivity() {

    abstract val TAG : String
    val binding: B by lazy { bindingFactory(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_PhotoWeather)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setListener()
    }

    abstract fun setListener()


}