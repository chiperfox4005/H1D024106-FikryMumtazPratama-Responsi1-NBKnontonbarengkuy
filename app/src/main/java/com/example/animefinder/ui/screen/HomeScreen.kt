package com.example.animefinder.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.animefinder.R
import com.example.animefinder.model.Anime
import com.example.animefinder.network.RetrofitClient
import com.example.animefinder.theme.BadgeRating
import com.example.animefinder.theme.PrimaryBlue
import com.example.animefinder.theme.PrimaryGreenTeal
import com.example.animefinder.theme.TextMuted
import com.example.animefinder.viewmodel.AnimeViewModel
import com.example.animefinder.viewmodel.UiState
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AnimeViewModel,
    onNavigateToDetail: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val searchState by viewModel.searchState.collectAsState()

    val genres = listOf(
        "Semua Genre" to null,
        "Action" to "1",
        "Adventure" to "2",
        "Comedy" to "4",
        "Drama" to "8",
        "Fantasy" to "10",
        "Romance" to "22",
        "Horror" to "14",
        "Sci-Fi" to "24",
        "Slice of Life" to "36",
        "Supernatural" to "37"
    )

    var selectedGenrePair by remember {
        mutableStateOf<Pair<String, String?>>(genres[0])
    }

    var dropdownExpanded by remember {
        mutableStateOf(false)
    }

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val coroutineScope = rememberCoroutineScope()

    var showProfileDialog by remember {
        mutableStateOf(false)
    }

    var showReadmeDialog by remember {
        mutableStateOf(false)
    }

    // ============================================================
    // RIGHT SIDE DRAWER
    // ============================================================

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {

                CompositionLocalProvider(
                    LocalLayoutDirection provides LayoutDirection.Ltr
                ) {

                    ModalDrawerSheet(
                        drawerContainerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.width(280.dp)
                    ) {

                        // ====================================================
                        // DRAWER HEADER
                        // ====================================================

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    PrimaryBlue.copy(alpha = 0.15f)
                                )
                                .padding(20.dp)
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Text(
                                    text = "Menu Pengaturan",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            drawerState.close()
                                        }
                                    }
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Tutup Menu",
                                        tint = TextMuted
                                    )
                                }
                            }
                        }

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        // ====================================================
                        // MENU PROFIL
                        // ====================================================

                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profil",
                                    tint = PrimaryBlue
                                )
                            },
                            label = {
                                Text(
                                    text = "Profil",
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            selected = false,
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.close()
                                }
                                showProfileDialog = true
                            },
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 4.dp
                            )
                        )

                        // ====================================================
                        // MENU README
                        // ====================================================

                        NavigationDrawerItem(
                            icon = {
                                Surface(
                                    color = PrimaryGreenTeal,
                                    shape = CircleShape,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "!",
                                            color = Color.White,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 15.sp
                                        )
                                    }
                                }
                            },
                            label = {
                                Text(
                                    text = "Readme / Info App",
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            selected = false,
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.close()
                                }
                                showReadmeDialog = true
                            },
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 4.dp
                            )
                        )
                    }
                }
            }
        ) {

            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Ltr
            ) {

                Scaffold(
                    // ========================================================
                    // TOP APP BAR
                    // ========================================================
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // LOGO NBK - Menggunakan Image
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_nbk_logo),
                                        contentDescription = "Logo NBK",
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                    )

                                    Spacer(
                                        modifier = Modifier.width(10.dp)
                                    )

                                    Text(
                                        text = "NBK Nonton Bareng Kuy",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            if (drawerState.isClosed) {
                                                drawerState.open()
                                            } else {
                                                drawerState.close()
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Pengaturan",
                                        tint = PrimaryBlue
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    }
                ) { paddingValues ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .background(
                                MaterialTheme.colorScheme.background
                            )
                    ) {

                        // ====================================================
                        // SEARCH BAR
                        // ====================================================

                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                            },
                            placeholder = {
                                Text(
                                    "Cari judul anime...",
                                    color = TextMuted
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        viewModel.searchAnime(
                                            searchQuery,
                                            selectedGenrePair.second
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Cari",
                                        tint = PrimaryBlue
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                ),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBlue,
                                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        // ====================================================
                        // GENRE DROPDOWN
                        // ====================================================

                        ExposedDropdownMenuBox(
                            expanded = dropdownExpanded,
                            onExpandedChange = {
                                dropdownExpanded = !dropdownExpanded
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 4.dp
                                )
                        ) {

                            OutlinedTextField(
                                value = selectedGenrePair.first,
                                onValueChange = {},
                                readOnly = true,
                                label = {
                                    Text(
                                        "Kategori Genre",
                                        color = PrimaryBlue
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = dropdownExpanded
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryBlue,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier
                                    .menuAnchor(
                                        ExposedDropdownMenuAnchorType.PrimaryNotEditable
                                    )
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = {
                                    dropdownExpanded = false
                                },
                                modifier = Modifier.background(
                                    MaterialTheme.colorScheme.surface
                                )
                            ) {
                                genres.forEach { genre ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = genre.first,
                                                fontWeight = if (genre == selectedGenrePair) FontWeight.Bold else FontWeight.Normal,
                                                color = if (genre == selectedGenrePair) PrimaryBlue else MaterialTheme.colorScheme.onSurface
                                            )
                                        },
                                        onClick = {
                                            selectedGenrePair = genre
                                            dropdownExpanded = false
                                            viewModel.searchAnime(
                                                searchQuery,
                                                genre.second
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        // ====================================================
                        // STATUS HEADER
                        // ====================================================

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Daftar Anime Terpopuler",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // ====================================================
                        // CONTENT
                        // ====================================================

                        when (val state = searchState) {
                            // ==================================================
                            // LOADING
                            // ==================================================
                            is UiState.Idle,
                            is UiState.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        CircularProgressIndicator(
                                            color = PrimaryBlue
                                        )
                                        Spacer(
                                            modifier = Modifier.height(12.dp)
                                        )
                                        Text(
                                            text = "Memuat data anime...",
                                            color = TextMuted
                                        )
                                    }
                                }
                            }

                            // ==================================================
                            // ERROR
                            // ==================================================
                            is UiState.Error -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = state.message,
                                            color = MaterialTheme.colorScheme.error,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                        Button(
                                            onClick = {
                                                viewModel.searchAnime(
                                                    searchQuery,
                                                    selectedGenrePair.second
                                                )
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = PrimaryBlue
                                            )
                                        ) {
                                            Text(
                                                text = "Coba Lagi",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            // ==================================================
                            // EMPTY
                            // ==================================================
                            is UiState.Empty -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Anime tidak ditemukan",
                                        color = TextMuted
                                    )
                                }
                            }

                            // ==================================================
                            // SUCCESS
                            // ==================================================
                            is UiState.Success -> {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    contentPadding = PaddingValues(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(state.data) { anime ->
                                        SimpleAnimeCard(
                                            anime = anime,
                                            onClick = {
                                                onNavigateToDetail(
                                                    anime.malId
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ================================================================
    // PROFILE DIALOG
    // ================================================================

    if (showProfileDialog) {
        AlertDialog(
            onDismissRequest = {
                showProfileDialog = false
            },
            // ============================================================
            // LOGO NBK
            // ============================================================
            icon = {
                Image(
                    painter = painterResource(
                        id = R.drawable.ic_nbk_logo
                    ),
                    contentDescription = "Logo NBK",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(18.dp))
                )
            },
            // ============================================================
            // TITLE
            // ============================================================
            title = {
                Text(
                    text = "Profil Pengguna",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            // ============================================================
            // PROFILE DATA
            // ============================================================
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                    ProfileItemRow(
                        label = "Nama",
                        value = "Member NBK"
                    )
                    ProfileItemRow(
                        label = "Aplikasi",
                        value = "NBK Nonton Bareng Kuy"
                    )
                    ProfileItemRow(
                        label = "Versi",
                        value = "v1.0.0"
                    )
                    ProfileItemRow(
                        label = "Status",
                        value = "Aktif"
                    )
                }
            },
            // ============================================================
            // CLOSE BUTTON
            // ============================================================
            confirmButton = {
                TextButton(
                    onClick = {
                        showProfileDialog = false
                    }
                ) {
                    Text(
                        text = "TUTUP",
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    // ================================================================
    // README DIALOG
    // ================================================================

    if (showReadmeDialog) {
        AlertDialog(
            onDismissRequest = {
                showReadmeDialog = false
            },
            icon = {
                Surface(
                    color = PrimaryGreenTeal,
                    shape = CircleShape,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "!",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                    }
                }
            },
            title = {
                Text(
                    text = "Readme / Petunjuk",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Text(
                        text = "1. Gunakan kolom pencarian untuk mencari anime berdasarkan judul.\n" +
                                "2. Pilih Dropdown Kategori Genre untuk memfilter anime.\n" +
                                "3. Klik pada kartu anime untuk melihat detail lengkap dan sinopsis.\n" +
                                "4. Data terintegrasi secara otomatis dari Jikan API (MyAnimeList).",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showReadmeDialog = false
                    }
                ) {
                    Text(
                        text = "MENGERTI",
                        color = PrimaryGreenTeal,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

// ====================================================================
// PROFILE ITEM ROW
// ====================================================================

@Composable
fun ProfileItemRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

// ====================================================================
// SIMPLE ANIME CARD
// ====================================================================

@Composable
fun SimpleAnimeCard(
    anime: Anime,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column {
            // ============================================================
            // POSTER
            // ============================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(anime.posterUrl)
                        .addHeader(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                                    "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                    "Chrome/120.0.0.0 Safari/537.36"
                        )
                        .crossfade(true)
                        .build(),
                    imageLoader = RetrofitClient.getImageLoader(context),
                    contentDescription = anime.title,
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = PrimaryBlue
                            )
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No Poster",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // ========================================================
                // TYPE TAG
                // ========================================================
                Surface(
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(
                        topStart = 10.dp,
                        bottomEnd = 8.dp
                    ),
                    modifier = Modifier.align(
                        Alignment.TopStart
                    )
                ) {
                    Text(
                        text = anime.type?.uppercase(Locale.US) ?: "ANIME",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            horizontal = 6.dp,
                            vertical = 2.dp
                        )
                    )
                }

                // ========================================================
                // RATING BADGE
                // ========================================================
                Surface(
                    color = BadgeRating,
                    shape = RoundedCornerShape(
                        bottomStart = 8.dp,
                        topEnd = 10.dp
                    ),
                    modifier = Modifier.align(
                        Alignment.TopEnd
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(
                            horizontal = 6.dp,
                            vertical = 2.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(
                            modifier = Modifier.width(2.dp)
                        )
                        Text(
                            text = if (anime.score != null && anime.score > 0) {
                                String.format(
                                    Locale.US,
                                    "%.1f",
                                    anime.score
                                )
                            } else {
                                "N/A"
                            },
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ============================================================
            // ANIME TITLE
            // ============================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}