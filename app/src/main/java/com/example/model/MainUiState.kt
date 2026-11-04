package com.example.model

data class MainUiState(
    val currentTool: ToolScreen = ToolScreen.HOME,
    val playlists: List<Playlist> = emptyList(),
    val isLoadingM3u: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null
)
