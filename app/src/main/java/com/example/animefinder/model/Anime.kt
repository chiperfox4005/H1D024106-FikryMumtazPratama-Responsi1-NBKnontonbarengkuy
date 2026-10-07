package com.example.animefinder.model

import com.google.gson.annotations.SerializedName

data class AnimeSearchResponse(
    val data: List<Anime>?
)

data class AnimeDetailResponse(
    val data: Anime?
)

data class Anime(
    @SerializedName("mal_id")
    val malId: Int,
    val title: String,
    @SerializedName("title_japanese")
    val titleJapanese: String? = null,
    val type: String? = null,
    val score: Double? = null,
    val episodes: Int? = null,
    val status: String? = null,
    val rating: String? = null,
    val duration: String? = null,
    val synopsis: String? = null,
    val images: AnimeImages? = null,
    val genres: List<Genre>? = null
) {
    val posterUrl: String?
        get() = images?.jpg?.largeImageUrl
            ?: images?.jpg?.imageUrl
            ?: images?.webp?.largeImageUrl
            ?: images?.webp?.imageUrl
}

data class AnimeImages(
    val jpg: ImageUrl? = null,
    val webp: ImageUrl? = null
)

data class ImageUrl(
    @SerializedName("image_url")
    val imageUrl: String? = null,
    @SerializedName("small_image_url")
    val smallImageUrl: String? = null,
    @SerializedName("large_image_url")
    val largeImageUrl: String? = null
)

data class Genre(
    @SerializedName("mal_id")
    val malId: Int,
    val name: String
)
