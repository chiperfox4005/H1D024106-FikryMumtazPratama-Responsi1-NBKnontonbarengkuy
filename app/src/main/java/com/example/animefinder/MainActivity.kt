package com.example.animefinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import coil.Coil
import coil.ImageLoader
import com.example.animefinder.navigation.AppNavigation
import com.example.animefinder.network.RetrofitClient
import com.example.animefinder.theme.AnimeFinderTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Setup Coil ImageLoader with OkHttpClient & User-Agent header
    val imageLoader = ImageLoader.Builder(this)
        .okHttpClient(RetrofitClient.okHttpClient)
        .crossfade(true)
        .build()
    Coil.setImageLoader(imageLoader)

    enableEdgeToEdge()
    setContent {
      AnimeFinderTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
          AppNavigation()
        }
      }
    }
  }
}
