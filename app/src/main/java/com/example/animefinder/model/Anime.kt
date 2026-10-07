package com.example.animefinder.model

import com.google.gson.annotations.SerializedName

// Response untuk pencarian / list anime
data class AnimeSearchResponse(
    @SerializedName("data")
    val data: List<Anime>? = emptyList(),

    @SerializedName("pagination")
    val pagination: Pagination? = null // Opsional: jika nanti ingin fitur load more / paging
)

// Response untuk detail anime tunggal
data class AnimeDetailResponse(
    @SerializedName("data")
    val data: Anime? = null
)

// Model utama Anime (Sama persis dengan schema Jikan v4 / Tenrai v1)
data class Anime(
    @SerializedName("mal_id")
    val malId: Int,

    val title: String,

    @SerializedName("title_japanese")
    val titleJapanese: String? = null,

    val type: String? = null, // TV, Movie, OVA, dll.

    val score: Double? = null,

    val episodes: Int? = null,

    val status: String? = null, // Finished Airing, Currently Airing, dll.

    val rating: String? = null, // PG-13, R, dll.

    val duration: String? = null, // "24 min per ep"

    val synopsis: String? = null,

    val year: Int? = null, // Tambahan opsional: Tahun rilis

    val season: String? = null, // Tambahan opsional: "spring", "fall", dll.

    val images: AnimeImages? = null,

    val genres: List<Genre>? = null
) {
    // Helper property untuk mendapatkan URL poster dengan fallback yang aman
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

// Tambahan opsional: Untuk fitur pagination jika diperlukan nanti
data class Pagination(
    @SerializedName("last_visible_page")
    val lastVisiblePage: Int,

    @SerializedName("has_next_page")
    val hasNextPage: Boolean,

    @SerializedName("current_page")
    val currentPage: Int,

    val items: Items? = null
)

data class Items(
    val count: Int,
    val total: Int,
    @SerializedName("per_page")
    val perPage: Int
)