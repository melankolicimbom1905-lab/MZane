package com.example.model

data class AccountInfo(
    val username: String = "",
    val email: String = "",
    val membershipType: String = "",
    val status: String = "",
    val expiryDate: String = "",
    val activeConnections: Int = 0,
    val maxConnections: Int = 0,
    val serverProtocol: String = ""
)

data class M3uPlaylist(
    val id: String = "",
    val name: String = "",
    val url: String = "",
    val isActive: Boolean = false
)

data class M3uChannel(
    val id: String = "",
    val name: String = "",
    val logoUrl: String = "",
    val streamUrl: String = "",
    val group: String = ""
)

data class M3uCategory(
    val name: String = "",
    val channels: List<M3uChannel> = emptyList()
)
