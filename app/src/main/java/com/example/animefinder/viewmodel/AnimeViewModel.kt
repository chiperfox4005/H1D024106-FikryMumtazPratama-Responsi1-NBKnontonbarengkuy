package com.example.animefinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.animefinder.model.Anime
import com.example.animefinder.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Empty : UiState<Nothing>()
}

class AnimeViewModel(private val repository: AnimeRepository) : ViewModel() {

    private val _searchState = MutableStateFlow<UiState<List<Anime>>>(UiState.Idle)
    val searchState: StateFlow<UiState<List<Anime>>> = _searchState.asStateFlow()

    private val _detailState = MutableStateFlow<UiState<Anime>>(UiState.Loading)
    val detailState: StateFlow<UiState<Anime>> = _detailState.asStateFlow()

    init {
        searchAnime("", null)
    }

    fun searchAnime(query: String, genreId: String?) {
        _searchState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = repository.searchAnime(query, genreId)
                if (result.isEmpty()) {
                    _searchState.value = UiState.Empty
                } else {
                    _searchState.value = UiState.Success(result)
                }
            } catch (e: UnknownHostException) {
                _searchState.value = UiState.Error("Tidak ada koneksi internet. Pastikan Wi-Fi atau Data Seluler di emulator/HP Anda dalam keadaan aktif.")
            } catch (e: IOException) {
                _searchState.value = UiState.Error("Koneksi internet terputus atau lambat. Silakan periksa koneksi Anda dan tekan Coba Lagi.")
            } catch (e: Exception) {
                _searchState.value = UiState.Error(e.message ?: "Gagal mengambil data dari server")
            }
        }
    }

    fun getAnimeDetail(id: Int) {
        _detailState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = repository.getAnimeDetail(id)
                if (result != null) {
                    _detailState.value = UiState.Success(result)
                } else {
                    _detailState.value = UiState.Empty
                }
            } catch (e: UnknownHostException) {
                _detailState.value = UiState.Error("Tidak ada koneksi internet. Pastikan Wi-Fi atau Data Seluler aktif.")
            } catch (e: IOException) {
                _detailState.value = UiState.Error("Koneksi gagal. Silakan coba lagi.")
            } catch (e: Exception) {
                _detailState.value = UiState.Error(e.message ?: "Gagal mengambil data")
            }
        }
    }
}

class AnimeViewModelFactory(private val repository: AnimeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AnimeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
