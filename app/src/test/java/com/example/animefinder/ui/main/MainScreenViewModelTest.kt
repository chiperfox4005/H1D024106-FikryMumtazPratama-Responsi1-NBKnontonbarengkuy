package com.example.animefinder.ui.main

import com.example.animefinder.model.AnimeDetailResponse
import com.example.animefinder.model.AnimeSearchResponse
import com.example.animefinder.network.TenraiApiService
import com.example.animefinder.repository.AnimeRepository
import com.example.animefinder.viewmodel.AnimeViewModel
import com.example.animefinder.viewmodel.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModelTest {

  private val testDispatcher = UnconfinedTestDispatcher()

  @Before
  fun setup() {
    Dispatchers.setMain(testDispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun animeViewModel_initialState_returnsState() = runTest {
    val repository = AnimeRepository(FakeJikanApiService())
    val viewModel = AnimeViewModel(repository)

    val state = viewModel.searchState.value
    assertTrue(state is UiState.Success || state is UiState.Loading || state is UiState.Empty)
  }
}

private class FakeJikanApiService : TenraiApiService {
  override suspend fun searchAnime(
    query: String?,
    genres: String?,
    orderBy: String?,
    sort: String?,
    sfw: Boolean
  ) = AnimeSearchResponse(data = emptyList())

  override suspend fun getTopAnime(sfw: Boolean) =
    AnimeSearchResponse(data = emptyList())

  override suspend fun getAnimeDetail(id: Int) =
    AnimeDetailResponse(data = null)
}
