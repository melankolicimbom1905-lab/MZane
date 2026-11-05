package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.BottomNavTab
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.ToolScreen

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val snackbarHostState = remember { SnackbarHostState() }

            // Show snackbar feedback whenever state message updates
            LaunchedEffect(uiState.snackbarMessage) {
                uiState.snackbarMessage?.let { msg ->
                    snackbarHostState.showSnackbar(msg)
                    viewModel.clearSnackbar()
                }
            }

            val context = LocalContext.current
            val radioPlayer = remember { MediaPlayer() }

            DisposableEffect(uiState.isRadioPlaying, uiState.currentRadioStation) {
                if (uiState.isRadioPlaying && uiState.currentRadioStation != null) {
                    try {
                        radioPlayer.reset()
                        radioPlayer.setDataSource(context, Uri.parse(uiState.currentRadioStation!!.streamUrl))
                        radioPlayer.setOnPreparedListener { it.start() }
                        radioPlayer.setOnErrorListener { _, _, _ -> true }
                        radioPlayer.prepareAsync()
                    } catch (e: Exception) {
                        // Gracefully ignore stream exceptions
                    }
                } else {
                    try {
                        if (radioPlayer.isPlaying) radioPlayer.pause()
                    } catch (e: Exception) {}
                }
                onDispose {
                    try {
                        radioPlayer.stop()
                        radioPlayer.reset()
                    } catch (e: Exception) {}
                }
            }

            MyApplicationTheme(selectedTheme = uiState.settings.theme) {
                // Back navigation handling
                BackHandler(
                    enabled = uiState.isPlayerOpen || uiState.isSearchOpen || uiState.activeToolScreen != null
                ) {
                    when {
                        uiState.isPlayerOpen -> viewModel.closePlayer()
                        uiState.isSearchOpen -> viewModel.toggleSearch(false)
                        uiState.activeToolScreen != null -> viewModel.closeTool()
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        if (!uiState.isPlayerOpen && uiState.activeToolScreen != ToolScreen.MULTI_SCREEN && uiState.activeToolScreen != ToolScreen.SETTINGS) {
                            TopBarView(
                                currentProfile = uiState.currentProfile,
                                onProfileClick = { viewModel.toggleProfileDialog(true) },
                                onSearchClick = { viewModel.toggleSearch(true) },
                                onQuickSettingsClick = { viewModel.toggleQuickSettingsDialog(true) }
                            )
                        }
                    },
                    bottomBar = {
                        if (!uiState.isPlayerOpen && uiState.activeToolScreen != ToolScreen.MULTI_SCREEN) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.testTag("bottom_nav_bar")
                            ) {
                                BottomNavTab.values().forEach { tab ->
                                    val isSelected = uiState.currentTab == tab && uiState.activeToolScreen == null
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = { viewModel.setTab(tab) },
                                        icon = {
                                            val icon = when (tab) {
                                                BottomNavTab.HOME -> Icons.Default.Home
                                                BottomNavTab.LIVE_TV -> Icons.Default.LiveTv
                                                BottomNavTab.MOVIES -> Icons.Default.Movie
                                                BottomNavTab.SERIES -> Icons.Default.VideoLibrary
                                                BottomNavTab.RADIO -> Icons.Default.Radio
                                                BottomNavTab.TOOLS -> Icons.Default.Apps
                                            }
                                            Icon(icon, contentDescription = tab.title)
                                        },
                                        label = {
                                            Text(
                                                text = tab.title,
                                                fontSize = 10.sp,
                                                maxLines = 1
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = Color.Black,
                                            selectedTextColor = MaterialTheme.colorScheme.primary,
                                            indicatorColor = MaterialTheme.colorScheme.primary,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(innerPadding)
                    ) {
                        // Main Navigation Tabs
                        when (uiState.currentTab) {
                            BottomNavTab.HOME -> {
                                HomeScreen(
                                    liveMatches = uiState.liveMatches,
                                    channels = uiState.channels,
                                    movies = uiState.movies,
                                    series = uiState.series,
                                    onSelectMatch = { viewModel.playLiveMatch(it) },
                                    onSelectChannel = { viewModel.playChannel(it) },
                                    onSelectMovie = { viewModel.playMovie(it) },
                                    onSelectSeries = {
                                        viewModel.setTab(BottomNavTab.SERIES)
                                    },
                                    onNavigateTab = { viewModel.setTab(it) }
                                )
                            }
                            BottomNavTab.LIVE_TV -> {
                                LiveTvScreen(
                                    channels = uiState.channels,
                                    settings = uiState.settings,
                                    onSelectChannel = { viewModel.playChannel(it) },
                                    onToggleFavorite = { viewModel.toggleChannelFavorite(it) }
                                )
                            }
                            BottomNavTab.MOVIES -> {
                                MoviesScreen(
                                    movies = uiState.movies,
                                    settings = uiState.settings,
                                    onPlayMovie = { viewModel.playMovie(it) },
                                    onToggleFavorite = { viewModel.toggleMovieFavorite(it) }
                                )
                            }
                            BottomNavTab.SERIES -> {
                                SeriesScreen(
                                    seriesList = uiState.series,
                                    onPlayEpisode = { s, ep -> viewModel.playEpisode(s, ep) }
                                )
                            }
                            BottomNavTab.RADIO -> {
                                RadioScreen(
                                    radios = uiState.radios,
                                    isPlaying = uiState.isRadioPlaying,
                                    currentStation = uiState.currentRadioStation,
                                    ambientThemes = uiState.ambientThemes,
                                    selectedAmbientTheme = uiState.selectedAmbientTheme,
                                    isAmbientEnabled = uiState.isAmbientVideoEnabled,
                                    onSelectAmbientTheme = { viewModel.selectAmbientTheme(it) },
                                    onToggleAmbientEnabled = { viewModel.toggleAmbientVideo(it) },
                                    onPlayStation = { viewModel.playRadio(it) },
                                    onTogglePlay = { viewModel.toggleRadioPlayback() }
                                )
                            }
                            BottomNavTab.TOOLS -> {
                                ToolsScreen(
                                    onSelectTool = { tool ->
                                        if (tool == ToolScreen.RADIO) {
                                            viewModel.setTab(BottomNavTab.RADIO)
                                        } else {
                                            viewModel.openTool(tool)
                                        }
                                    }
                                )
                            }
                        }

                        // Specific Tool Overlay Screens
                        uiState.activeToolScreen?.let { tool ->
                            when (tool) {
                                ToolScreen.PREMIUM -> {
                                    PremiumScreen(
                                        isPremiumActive = uiState.isPremiumActive,
                                        onUnlockWithCode = { viewModel.unlockPremiumWithAdminCode(it) },
                                        onBack = { viewModel.closeTool() },
                                        onSupportClick = {
                                            viewModel.closeTool()
                                        }
                                    )
                                }
                                ToolScreen.ACCOUNT -> {
                                    AccountScreen(
                                        accountInfo = com.example.model.AccountInfo(),
                                        playlists = uiState.playlists,
                                        isLoadingM3u = uiState.isLoadingM3u,
                                        onAddPlaylist = { name, url -> viewModel.importM3uPlaylist(name, url) },
                                        onActivateAndWatch = { viewModel.activatePlaylistAndWatch(it) },
                                        onDeletePlaylist = { viewModel.deletePlaylist(it) },
                                        onBack = { viewModel.closeTool() }
                                    )
                                }
                                ToolScreen.SETTINGS -> {
                                    SettingsScreen(
                                        settings = uiState.settings,
                                        cacheSizeMb = uiState.cacheSizeMb,
                                        speedTestState = uiState.speedTestState,
                                        onUpdateSettings = { viewModel.updateSettings(it) },
                                        onClearCache = { viewModel.clearCache() },
                                        onClearHistory = { viewModel.clearHistory() },
                                        onBackupData = { viewModel.backupData() },
                                        onRestoreData = { viewModel.restoreData() },
                                        onBackupGoogle = { viewModel.backupToGoogleDrive() },
                                        onRestoreGoogle = { viewModel.restoreFromGoogleDrive() },
                                        onResetAll = { viewModel.resetAllSettings() },
                                        onStartSpeedTest = { viewModel.startSpeedTest() },
                                        onRefreshEpg = { viewModel.refreshEpg() },
                                        onToggleTrakt = { viewModel.toggleTraktConnection() },
                                        onBack = { viewModel.closeTool() }
                                    )
                                }
                                ToolScreen.KIDS_MODE -> {
                                    KidsModeScreen(
                                        isKidsActive = uiState.settings.kidsModeActive,
                                        onToggleKids = { viewModel.toggleKidsMode(it) },
                                        onBack = { viewModel.closeTool() }
                                    )
                                }
                                ToolScreen.FOLDERS -> {
                                    FoldersScreen(
                                        folders = uiState.settings.customFolders,
                                        onBack = { viewModel.closeTool() }
                                    )
                                }
                                ToolScreen.MULTI_SCREEN -> {
                                    MultiScreenView(
                                        screenCount = uiState.multiScreenCount,
                                        slots = uiState.multiScreenSlots,
                                        allChannels = uiState.channels,
                                        activeAudioIndex = uiState.activeAudioSlotIndex,
                                        onSelectScreenCount = { viewModel.setMultiScreenCount(it) },
                                        onSelectAudioSlot = { viewModel.setActiveAudioSlot(it) },
                                        onAssignSlotChannel = { slot, ch -> viewModel.setMultiScreenSlotChannel(slot, ch) },
                                        onClose = { viewModel.closeTool() }
                                    )
                                }
                                ToolScreen.EPG -> {
                                    EpgScreen(
                                        channels = uiState.channels,
                                        onRefreshEpg = { viewModel.refreshEpg() },
                                        onBack = { viewModel.closeTool() }
                                    )
                                }
                                ToolScreen.STATS -> {
                                    StatsScreen(
                                        todayMin = uiState.watchTimeTodayMin,
                                        weekHours = uiState.watchTimeWeekHours,
                                        onBack = { viewModel.closeTool() }
                                    )
                                }
                                ToolScreen.WHAT_TO_WATCH -> {
                                    WhatToWatchScreen(
                                        recommendation = uiState.recommendationItem,
                                        onGenerate = { viewModel.generateRecommendation() },
                                        onBack = { viewModel.closeTool() }
                                    )
                                }
                                ToolScreen.RECENT_ADDED -> {
                                    RecentAddedScreen(
                                        movies = uiState.movies,
                                        series = uiState.series,
                                        onBack = { viewModel.closeTool() }
                                    )
                                }
                                ToolScreen.SPORTS_GUIDE -> {
                                    SportsGuideScreen(onBack = { viewModel.closeTool() })
                                }
                                ToolScreen.SERIES_CALENDAR -> {
                                    SeriesCalendarScreen(onBack = { viewModel.closeTool() })
                                }
                                ToolScreen.SPORTS_RADAR -> {
                                    MackolikScreen(onBack = { viewModel.closeTool() })
                                }
                                ToolScreen.RADIO -> {
                                    // Handled via BottomNavTab.RADIO
                                }
                                ToolScreen.ABOUT -> {
                                    AboutScreen(onBack = { viewModel.closeTool() })
                                }
                            }
                        }

                        // Full Screen Single Stream Player
                        if (uiState.isPlayerOpen && uiState.currentPlayingUrl != null) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                VideoPlayerView(
                                    title = uiState.currentPlayingTitle ?: "Yayın",
                                    subtitle = uiState.currentPlayingSubtitle,
                                    videoUrl = uiState.currentPlayingUrl!!,
                                    settings = uiState.settings,
                                    onClose = { viewModel.closePlayer() }
                                )
                                // Sol / sağ kanal değiştirme butonları
                                if (uiState.channels.any { it.name == uiState.currentPlayingTitle }) {
                                    IconButton(
                                        onClick = { viewModel.playPreviousChannel() },
                                        modifier = Modifier
                                            .align(Alignment.CenterStart)
                                            .padding(start = 8.dp)
                                            .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                                    ) {
                                        Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Önceki kanal", tint = Color.White)
                                    }
                                    IconButton(
                                        onClick = { viewModel.playNextChannel() },
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(end = 8.dp)
                                            .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                                    ) {
                                        Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Sonraki kanal", tint = Color.White)
                                    }
                                }
                            }
                        }

                        // Universal Search Modal
                        if (uiState.isSearchOpen) {
                            SearchModal(
                                searchQuery = uiState.searchQuery,
                                onQueryChange = { viewModel.updateSearchQuery(it) },
                                channels = uiState.channels,
                                movies = uiState.movies,
                                series = uiState.series,
                                onSelectChannel = {
                                    viewModel.toggleSearch(false)
                                    viewModel.playChannel(it)
                                },
                                onSelectMovie = {
                                    viewModel.toggleSearch(false)
                                    viewModel.playMovie(it)
                                },
                                onSelectSeries = {
                                    viewModel.toggleSearch(false)
                                    viewModel.setTab(BottomNavTab.SERIES)
                                },
                                onClose = { viewModel.toggleSearch(false) }
                            )
                        }

                        // Profile Switcher Dialog
                        if (uiState.showProfileDialog) {
                            ProfileDialog(
                                profiles = uiState.profiles,
                                currentProfile = uiState.currentProfile,
                                onSelectProfile = { viewModel.setProfile(it) },
                                onDismiss = { viewModel.toggleProfileDialog(false) }
                            )
                        }

                        // Quick Settings Dialog
                        if (uiState.showQuickSettingsDialog) {
                            QuickSettingsDialog(
                                settings = uiState.settings,
                                onOpenFullSettings = {
                                    viewModel.openTool(ToolScreen.SETTINGS)
                                },
                                onToggleAdult = { viewModel.toggleAdultContent(it) },
                                onToggleKids = { viewModel.toggleKidsMode(it) },
                                onSelectTheme = { viewModel.updateTheme(it) },
                                onDismiss = { viewModel.toggleQuickSettingsDialog(false) }
                            )
                        }
                    }
                }
            }
        }
    }
}
