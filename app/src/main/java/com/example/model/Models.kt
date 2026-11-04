package com.example.model

enum class ThemeType(val displayName: String, val description: String) {
    TITANIUM_DARK("Modern Koyu", "Varsayılan titanyum koyu mod"),
    AMOLED("AMOLED Gece Siyahı", "Saf siyah, OLED ekranlar için pil tasarrufu"),
    OCEAN_BLUE("Okyanus Mavisi", "Derin lacivert ve turkuaz tonları"),
    CYBERPUNK("Siberpunk", "Canlı neon mor ve mavi kontrastı"),
    GOLD_VIP("Gold VIP", "Lüks altın ve karbon siyah görünüm")
}

enum class BufferMode(val displayName: String, val bufferSeconds: Int) {
    LOW("Düşük (1.5 sn - Hızlı Açılış)", 1500),
    BALANCED("Dengeli (3.5 sn - Tavsiye Edilen)", 3500),
    HIGH("Yüksek (6.0 sn - Zayıf Bağlantı)", 6000)
}

enum class DnsProvider(val displayName: String, val ip: String) {
    AUTO("Otomatik (Varsayılan Ağ)", "Sistem"),
    CLOUDFLARE("Cloudflare DNS", "1.1.1.1"),
    GOOGLE("Google DNS", "8.8.8.8"),
    QUAD9("Quad9 Güvenli DNS", "9.9.9.9"),
    ADGUARD("AdGuard DNS (Reklam Engelleyici)", "94.140.14.14")
}

enum class BottomNavTab(val title: String) {
    HOME("Ana Sayfa"),
    LIVE_TV("Canlı TV"),
    MOVIES("Filmler"),
    SERIES("Diziler"),
    RADIO("Radyo"),
    TOOLS("Araçlar")
}

data class UserProfile(
    val id: String,
    val name: String,
    val avatarColorHex: Long,
    val isKids: Boolean = false
)

data class Channel(
    val id: String,
    val channelNumber: Int,
    val name: String,
    val category: String,
    val streamUrl: String,
    val logoText: String,
    val currentEpg: String,
    val nextEpg: String,
    val isFavorite: Boolean = false,
    val resolution: String = "FHD",
    val isAdult: Boolean = false
)

data class Movie(
    val id: String,
    val title: String,
    val category: String,
    val year: Int,
    val duration: String,
    val rating: Double,
    val plot: String,
    val genre: String,
    val videoUrl: String,
    val isNew: Boolean = false,
    val isFavorite: Boolean = false
)

data class Episode(
    val season: Int,
    val episodeNumber: Int,
    val title: String,
    val duration: String,
    val airDate: String,
    val videoUrl: String
)

data class Series(
    val id: String,
    val title: String,
    val category: String,
    val rating: Double,
    val totalSeasons: Int,
    val plot: String,
    val episodes: List<Episode>,
    val isNew: Boolean = false,
    val isFavorite: Boolean = false
)

data class RadioStation(
    val id: String,
    val name: String,
    val genre: String,
    val frequency: String,
    val streamUrl: String,
    val currentTrack: String,
    val isFavorite: Boolean = false
)

data class AmbientTheme(
    val id: String,
    val title: String,
    val category: String,
    val iconEmoji: String,
    val description: String,
    val videoUrl: String,
    val effectType: String
)

data class LiveMatch(
    val id: String,
    val team1: String,
    val team2: String,
    val league: String,
    val matchTime: String,
    val score: String,
    val isLive: Boolean,
    val minute: String,
    val channelName: String,
    val streamUrl: String
)

data class EpgProgram(
    val id: String,
    val channelId: String,
    val title: String,
    val startTime: String,
    val endTime: String,
    val category: String,
    val progress: Float = 0.4f
)

data class SeriesCalendarItem(
    val id: String,
    val seriesTitle: String,
    val episodeTitle: String,
    val releaseDate: String,
    val dayName: String,
    val time: String,
    val channel: String
)

data class SportsRadarItem(
    val id: String,
    val homeTeam: String,
    val awayTeam: String,
    val league: String,
    val minute: String,
    val homeScore: Int,
    val awayScore: Int,
    val status: String,
    val lastAction: String
)

data class CustomFolder(
    val id: String,
    val name: String,
    val iconName: String,
    val channelIds: List<String> = emptyList()
)

data class AccountInfo(
    val serverUrl: String = "http://stream.globalcdn.live:8080",
    val username: String = "vip_abone_2026",
    val status: String = "Aktif (Premium)",
    val expiryDate: String = "18.11.2027",
    val maxConnections: Int = 3,
    val activeConnections: Int = 1,
    val m3uUrl: String = "http://stream.globalcdn.live:8080/get.php?username=vip&type=m3u_plus",
    val serverProtocol: String = "HLS / MPEG-TS / VOD"
)

data class AppSettings(
    val theme: ThemeType = ThemeType.TITANIUM_DARK,
    val showGenresMenu: Boolean = true,
    val hideAdult: Boolean = true,
    val advancedPlayerStats: Boolean = true,
    val traktConnected: Boolean = false,
    val traktUsername: String = "Kullanıcı",
    val kidsModeActive: Boolean = false,
    val kidsPin: String = "1234",
    val playerColumns: Int = 2,
    val liteView: Boolean = false,
    val subtitleSize: Float = 16f,
    val subtitleColor: String = "Sarı (#FFE600)",
    val hwAcceleration: Boolean = true,
    val autoPiP: Boolean = true,
    val allowLandscape: Boolean = true,
    val orientationLock: String = "Sensöre Göre Otomatik",
    val customEpgUrl: String = "https://epg.streamhub.tv/guide.xml",
    val timeShiftHours: Int = 0,
    val swipeChannelSwitch: Boolean = true,
    val bufferMode: BufferMode = BufferMode.BALANCED,
    val dnsProvider: DnsProvider = DnsProvider.AUTO,
    val parentalPin: String = "0000",
    val appLanguage: String = "Türkçe",
    val splashVideo: Boolean = false,
    val defaultStartScreen: String = "Ana Sayfa",
    val customFolders: List<CustomFolder> = listOf(
        CustomFolder("f1", "Favori Spor Kanalları", "sports", listOf("c1", "c2", "c3")),
        CustomFolder("f2", "Haftasonu Sineması", "movie", listOf("c4", "c5")),
        CustomFolder("f3", "Haber ve Gündem", "news", listOf("c6", "c7"))
    )
)
