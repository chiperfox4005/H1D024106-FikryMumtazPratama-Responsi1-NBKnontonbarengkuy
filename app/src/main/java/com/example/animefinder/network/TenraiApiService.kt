package com.example.animefinder.network

import com.example.animefinder.model.AnimeDetailResponse
import com.example.animefinder.model.AnimeSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TenraiApiService {

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String? = null, // <--- UBAH MENJADI String?
        @Query("limit") limit: Int = 25,
        @Query("genres") genre: String? = null
    ): Response<AnimeSearchResponse>

    @GET("anime/{id}")
    suspend fun getAnimeById(
        @Path("id") id: Int
    ): Response<AnimeDetailResponse>

    @GET("anime")
    suspend fun getTopAnime(
        @Query("order_by") orderBy: String = "score",
        @Query("sort") sort: String = "desc",
        @Query("limit") limit: Int = 25,
        @Query("filter") filter: String? = null
    ): Response<AnimeSearchResponse>
}