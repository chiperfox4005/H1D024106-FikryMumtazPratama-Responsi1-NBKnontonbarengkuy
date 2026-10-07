package com.example.animefinder

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.example.animefinder.network.RetrofitClient

class AnimeApplication : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return RetrofitClient.getImageLoader(this)
    }
}
