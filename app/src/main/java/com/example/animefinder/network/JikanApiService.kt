package com.example.animefinder.network

import com.example.animefinder.model.AnimeDetailResponse
import com.example.animefinder.model.AnimeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {
    
    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String? = null,
        @Query("genres") genres: String? = null,
        @Query("order_by") orderBy: String? = "popularity",
        @Query("sort") sort: String? = "asc",
        @Query("sfw") sfw: Boolean = true
    ): AnimeSearchResponse

    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("sfw") sfw: Boolean = true
    ): AnimeSearchResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetail(
        @Path("id") id: Int
    ): AnimeDetailResponse
}
