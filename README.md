# Anime Finder

Aplikasi Android sederhana untuk mencari dan melihat detail anime menggunakan Jikan API. Aplikasi ini dibuat sebagai tugas responsi dengan menerapkan arsitektur MVVM dan Jetpack Compose.

## Fitur
* Pencarian anime berdasarkan judul.
* Filter berdasarkan genre (Action, Adventure, dll).
* Daftar hasil pencarian anime.
* Detail informasi anime (Sinopsis, Rating, Episode, dll).

## Teknologi
* **Bahasa:** Kotlin
* **UI:** Jetpack Compose (dengan Material Design 3)
* **Arsitektur:** MVVM (Model-View-ViewModel)
* **Networking:** Retrofit & Gson
* **Image Loading:** Coil
* **Asynchronous:** Coroutines & Flow
* **API:** Jikan API (V4)

## Penjelasan MVVM
* **Model:** Menggunakan Data Class Kotlin untuk memetakan response JSON dari Jikan API (seperti `Anime`, `AnimeSearchResponse`).
* **Repository:** `AnimeRepository` bertugas sebagai penengah (Single Source of Truth) yang memanggil `JikanApiService`.
* **ViewModel:** `AnimeViewModel` mengatur logika bisnis dan menyimpan State (Loading, Success, Error) menggunakan `StateFlow`. UI akan bereaksi terhadap perubahan state ini (State-driven UI).
* **View (UI):** Diimplementasikan menggunakan Jetpack Compose (`HomeScreen`, `DetailScreen`). UI hanya menampilkan data dari ViewModel dan tidak memanggil API secara langsung.

## Penggunaan Jikan API
Aplikasi menggunakan REST API dari Jikan:
1. **Search Anime:** `GET https://api.jikan.moe/v4/anime?q={query}&genres={genre}`
2. **Detail Anime:** `GET https://api.jikan.moe/v4/anime/{id}`

Aplikasi mengambil data JSON dari endpoint tersebut lalu mengubahnya menjadi object Kotlin untuk ditampilkan ke layar.
