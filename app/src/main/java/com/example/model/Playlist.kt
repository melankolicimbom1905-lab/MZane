package com.example.model

data class Playlist(
    val id: String,
    val name: String,
    val url: String,
    val isActive: Boolean = false
)
