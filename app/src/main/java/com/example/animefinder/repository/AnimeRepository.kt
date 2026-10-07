package com.example.animefinder.repository

import com.example.animefinder.model.Anime
import com.example.animefinder.model.AnimeImages
import com.example.animefinder.model.Genre
import com.example.animefinder.model.ImageUrl
import com.example.animefinder.network.TenraiApiService

class AnimeRepository(private val apiService: TenraiApiService) {

    suspend fun searchAnime(query: String?, genreId: String?): List<Anime> {
        val cleanQuery = if (query.isNullOrBlank()) null else query.trim()
        return try {
            val response = if (cleanQuery == null && genreId == null) {
                apiService.getTopAnime()
            } else {
                // PERBAIKAN 1: 'genres' diganti menjadi 'genre' sesuai TenraiApiService
                apiService.searchAnime(query = cleanQuery, genre = genreId)
            }

            // PERBAIKAN 2: Tambahkan .body() sebelum .data
            val data = response.body()?.data

            if (data.isNullOrEmpty()) {
                filterFallback(cleanQuery, genreId)
            } else {
                data
            }
        } catch (e: Exception) {
            filterFallback(cleanQuery, genreId)
        }
    }

    suspend fun getAnimeDetail(id: Int): Anime? {
        return try {
            // PERBAIKAN 3: 'getAnimeDetail' diganti menjadi 'getAnimeById'
            val response = apiService.getAnimeById(id = id)

            // PERBAIKAN 4: Tambahkan .body() sebelum .data
            response.body()?.data ?: fallbackAnimeList.find { it.malId == id }
        } catch (e: Exception) {
            fallbackAnimeList.find { it.malId == id } ?: fallbackAnimeList.first()
        }
    }

    private fun filterFallback(query: String?, genreId: String?): List<Anime> {
        var list = fallbackAnimeList
        if (!query.isNullOrBlank()) {
            list = list.filter { it.title.contains(query, ignoreCase = true) }
        }
        if (!genreId.isNullOrBlank()) {
            list = list.filter { anime ->
                anime.genres?.any { it.malId.toString() == genreId } == true
            }
        }
        return list
    }

    companion object {
        val fallbackAnimeList = listOf(
            Anime(
                malId = 101,
                title = "Jujutsu Kaisen 0: The Movie",
                type = "Movie",
                score = 8.4,
                episodes = 1,
                status = "Finished Airing",
                rating = "13+",
                duration = "105 min",
                synopsis = "Yuta Okkotsu, a high school student who gains control of an extremely powerful Cursed Spirit and gets enrolled in the Tokyo Prefectural Jujutsu High School by Jujutsu Sorcerers to help him control his power and keep an eye on him.",
                images = AnimeImages(
                    jpg = ImageUrl(
                        imageUrl = "https://cdn.myanimelist.net/images/anime/1121/119044.jpg",
                        largeImageUrl = "https://cdn.myanimelist.net/images/anime/1121/119044.jpg"
                    )
                ),
                genres = listOf(Genre(1, "Action"), Genre(10, "Fantasy"))
            ),
            Anime(
                malId = 102,
                title = "Demon Slayer: Kimetsu no Yaiba - Hashira Training",
                type = "Movie",
                score = 8.6,
                episodes = 1,
                status = "Finished Airing",
                rating = "17+",
                duration = "104 min",
                synopsis = "Tanjiro undergoes rigorous training with the Hashira to prepare for the upcoming battle against Muzan Kibutsuji.",
                images = AnimeImages(
                    jpg = ImageUrl(
                        imageUrl = "https://cdn.myanimelist.net/images/anime/1765/140900.jpg",
                        largeImageUrl = "https://cdn.myanimelist.net/images/anime/1765/140900.jpg"
                    )
                ),
                genres = listOf(Genre(1, "Action"), Genre(37, "Supernatural"))
            ),
            Anime(
                malId = 103,
                title = "Attack on Titan: The Final Season",
                type = "TV",
                score = 9.0,
                episodes = 28,
                status = "Finished Airing",
                rating = "17+",
                duration = "24 min per ep",
                synopsis = "Gabi Braun and Falco Grice have been training their entire lives to inherit one of the seven Titans under Marley's control and aid their nation in eradicating the Eldians on Paradis.",
                images = AnimeImages(
                    jpg = ImageUrl(
                        imageUrl = "https://cdn.myanimelist.net/images/anime/1000/110531.jpg",
                        largeImageUrl = "https://cdn.myanimelist.net/images/anime/1000/110531.jpg"
                    )
                ),
                genres = listOf(Genre(1, "Action"), Genre(8, "Drama"))
            ),
            Anime(
                malId = 104,
                title = "Your Name. (Kimi no Na wa.)",
                type = "Movie",
                score = 8.9,
                episodes = 1,
                status = "Finished Airing",
                rating = "SU",
                duration = "106 min",
                synopsis = "Mitsuha Miyamizu, a high school girl, yearns to live the life of a boy in the bustling city of Tokyo—a dream that stands in stark contrast to her present life in the countryside.",
                images = AnimeImages(
                    jpg = ImageUrl(
                        imageUrl = "https://cdn.myanimelist.net/images/anime/1935/127974.jpg",
                        largeImageUrl = "https://cdn.myanimelist.net/images/anime/1935/127974.jpg"
                    )
                ),
                genres = listOf(Genre(22, "Romance"), Genre(8, "Drama"))
            ),
            Anime(
                malId = 105,
                title = "Suzume (Suzume no Tojimari)",
                type = "Movie",
                score = 8.3,
                episodes = 1,
                status = "Finished Airing",
                rating = "13+",
                duration = "121 min",
                synopsis = "A 17-year-old girl named Suzume helps a mysterious young man close doors from the other side that are releasing disasters all over Japan.",
                images = AnimeImages(
                    jpg = ImageUrl(
                        imageUrl = "https://cdn.myanimelist.net/images/anime/1825/129202.jpg",
                        largeImageUrl = "https://cdn.myanimelist.net/images/anime/1825/129202.jpg"
                    )
                ),
                genres = listOf(Genre(10, "Fantasy"), Genre(2, "Adventure"))
            ),
            Anime(
                malId = 106,
                title = "SPY x FAMILY Code: White",
                type = "Movie",
                score = 8.1,
                episodes = 1,
                status = "Finished Airing",
                rating = "SU",
                duration = "110 min",
                synopsis = "After receiving an order to be replaced in Operation Strix, Loid decides to help Anya win a cooking competition at Eden Academy by making the principal's favorite dessert.",
                images = AnimeImages(
                    jpg = ImageUrl(
                        imageUrl = "https://cdn.myanimelist.net/images/anime/1023/139268.jpg",
                        largeImageUrl = "https://cdn.myanimelist.net/images/anime/1023/139268.jpg"
                    )
                ),
                genres = listOf(Genre(4, "Comedy"), Genre(1, "Action"))
            )
        )
    }
}