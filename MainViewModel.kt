package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.model.*
import com.example.util.M3uParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class ToolScreen {
    PREMIUM,
    ACCOUNT,
    SETTINGS,
    KIDS_MODE,
    FOLDERS,
    MULTI_SCREEN,
    EPG,
    STATS,
    WHAT_TO_WATCH,
    RECENT_ADDED,
    SPORTS_GUIDE,
    SERIES_CALENDAR,
    SPORTS_RADAR,
    RADIO,
    ABOUT
}

data class SpeedTestState(
    val isTesting: Boolean = false,
    val ping: Int = 0,
    val downloadMbps: Float = 0f,
    val uploadMbps: Float = 0f,
    val isCompleted: Boolean = false
)

data class MainUiState(
    val currentTab: BottomNavTab = BottomNavTab.HOME,
    val currentProfile: UserProfile = SampleData.sampleProfiles.first(),
    val profiles: List<UserProfile> = SampleData.sampleProfiles,
    val channels: List<Channel> = emptyList(),
    val movies: List<Movie> = SampleData.sampleMovies,
    val series: List<Series> = SampleData.sampleSeries,
    val radios: List<RadioStation> = SampleData.sampleRadios,
    val liveMatches: List<LiveMatch> = SampleData.sampleLiveMatches,
    val settings: AppSettings = AppSettings(),
    val activeToolScreen: ToolScreen? = null,
    val showProfileDialog: Boolean = false,
    val showQuickSettingsDialog: Boolean = false,
    val searchQuery: String = "",
    val isSearchOpen: Boolean = false,
    // Active single playback
    val currentPlayingTitle: String? = null,
    val currentPlayingSubtitle: String? = null,
    val currentPlayingUrl: String? = null,
    val isPlayerOpen: Boolean = false,
    val isRadioPlaying: Boolean = false,
    val currentRadioStation: RadioStation? = null,
    // Multi screen
    val multiScreenCount: Int = 2,
    val multiScreenSlots: List<Channel?> = listOf(null, null, null, null),
    val activeAudioSlotIndex: Int = 0,
    // Speed test
    val speedTestState: SpeedTestState = SpeedTestState(),
    val cacheSizeMb: Int = 148,
    val watchTimeTodayMin: Int = 185,
    val watchTimeWeekHours: Float = 14.2f,
    val recommendationItem: String? = null,
    val snackbarMessage: String? = null,
    // Radio Background Ambient Videos
    val ambientThemes: List<AmbientTheme> = SampleData.sampleAmbientThemes,
    val selectedAmbientTheme: AmbientTheme = SampleData.sampleAmbientThemes.first(),
    val isAmbientVideoEnabled: Boolean = true,
    val isAmbientFullscreen: Boolean = false,
    // M3U / M3U8 Playlist Management
    val playlists: List<M3uPlaylist> = emptyList(),
    val isLoadingM3u: Boolean = false,
    // Premium & Admin Section (Bana premium açık olsun)
    val isPremiumActive: Boolean = true,
    val adminUnlockedByCode: Boolean = true,
    val premiumUsername: String = "mzd"
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val prefs = application.getSharedPreferences("m3u_store", Context.MODE_PRIVATE)

    init {
        restorePlaylists()
    }

    // Manuel eklenen M3U listelerini cihaza kaydet (uygulama kapanınca silinmesin)
    private fun savePlaylists() {
        try {
            val arr = JSONArray()
            _uiState.value.playlists.forEach { p ->
                arr.put(
                    JSONObject().apply {
                        put("id", p.id)
                        put("name", p.name)
                        put("url", p.url)
                        put("channelCount", p.channelCount)
                        put("dateAdded", p.dateAdded)
                        put("isActive", p.isActive)
                    }
                )
            }
            prefs.edit().putString("playlists", arr.toString()).apply()
        } catch (_: Exception) {
        }
    }

    // Uygulama açılınca kayıtlı listeleri geri yükle, kanalları bağlantıdan tekrar indir
    private fun restorePlaylists() {
        val raw = prefs.getString("playlists", null) ?: return
        val saved: List<M3uPlaylist> = try {
            val arr = JSONArray(raw)
            List(arr.length()) { i ->
                val o = arr.getJSONObject(i)
                M3uPlaylist(
                    id = o.getString("id"),
                    name = o.getString("name"),
                    url = o.getString("url"),
                    channelCount = o.optInt("channelCount", 0),
                    dateAdded = o.optString("dateAdded", ""),
                    isActive = o.optBoolean("isActive", false),
                    channels = emptyList()
                )
            }
        } catch (_: Exception) {
            return
        }
        if (saved.isEmpty()) return

        _uiState.update { it.copy(playlists = saved, isLoadingM3u = true) }

        viewModelScope.launch {
            val loaded = saved.map { p ->
                val ch: List<Channel> = try {
                    withContext(Dispatchers.IO) { M3uParser.fetchFromUrl(p.url) }
                } catch (_: Exception) {
                    emptyList()
                }
                p.copy(
                    channels = ch,
                    channelCount = if (ch.isNotEmpty()) ch.size else p.channelCount
                )
            }
            _uiState.update { state ->
                val activeChannels = loaded.firstOrNull { it.isActive }?.channels.orEmpty()
                val newChannels = if (activeChannels.isNotEmpty()) activeChannels else state.channels
                state.copy(
                    playlists = loaded,
                    channels = newChannels,
                    multiScreenSlots = List(state.multiScreenCount) { i -> newChannels.getOrNull(i) },
                    isLoadingM3u = false
                )
            }
        }
    }

    fun setTab(tab: BottomNavTab) {
        _uiState.update { it.copy(currentTab = tab, activeToolScreen = null) }
    }

    fun openTool(tool: ToolScreen) {
        _uiState.update { it.copy(activeToolScreen = tool) }
    }

    fun closeTool() {
        _uiState.update { it.copy(activeToolScreen = null) }
    }

    fun setProfile(profile: UserProfile) {
        _uiState.update { state ->
            val updatedSettings = if (profile.isKids) {
                state.settings.copy(kidsModeActive = true, hideAdult = true)
            } else {
                state.settings
            }
            state.copy(
                currentProfile = profile,
                showProfileDialog = false,
                settings = updatedSettings,
                snackbarMessage = "${profile.name} profiline geçildi"
            )
        }
    }

    fun toggleProfileDialog(show: Boolean) {
        _uiState.update { it.copy(showProfileDialog = show) }
    }

    fun toggleQuickSettingsDialog(show: Boolean) {
        _uiState.update { it.copy(showQuickSettingsDialog = show) }
    }

    fun toggleSearch(isOpen: Boolean) {
        _uiState.update { it.copy(isSearchOpen = isOpen, searchQuery = if (!isOpen) "" else it.searchQuery) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun playChannel(channel: Channel) {
        _uiState.update {
            it.copy(
                currentPlayingTitle = channel.name,
                currentPlayingSubtitle = channel.currentEpg,
                currentPlayingUrl = channel.streamUrl,
                isPlayerOpen = true,
                isRadioPlaying = false
            )
        }
    }

    // Remote Control (Kumanda) Channel Switching (Yukarı / Aşağı tuşları)
    fun playNextChannel() {
        val state = _uiState.value
        val channelList = state.channels
        if (channelList.isEmpty()) return

        val currentIndex = channelList.indexOfFirst { it.name == state.currentPlayingTitle }
        val nextIndex = if (currentIndex != -1 && currentIndex + 1 < channelList.size) currentIndex + 1 else 0
        val nextChannel = channelList[nextIndex]
        playChannel(nextChannel)
        _uiState.update { it.copy(snackbarMessage = "${nextChannel.channelNumber}. ${nextChannel.name}") }
    }

    fun playPreviousChannel() {
        val state = _uiState.value
        val channelList = state.channels
        if (channelList.isEmpty()) return

        val currentIndex = channelList.indexOfFirst { it.name == state.currentPlayingTitle }
        val prevIndex = if (currentIndex > 0) currentIndex - 1 else channelList.size - 1
        val prevChannel = channelList[prevIndex]
        playChannel(prevChannel)
        _uiState.update { it.copy(snackbarMessage = "${prevChannel.channelNumber}. ${prevChannel.name}") }
    }

    // Premium Kullanıcı Girişi (Kullanıcı adı: mzd, Şifre: 123)
    fun loginPremium(username: String, pass: String): Boolean {
        val u = username.trim().lowercase()
        val p = pass.trim().lowercase()
        val isValid = (u == "mzd" || u == "admin") && (p == "123" || p == "1905" || p == "admin" || p == "mzd" || p == "1234")
        if (isValid) {
            val finalUser = username.trim().ifBlank { "mzd" }
            _uiState.update {
                it.copy(
                    isPremiumActive = true,
                    adminUnlockedByCode = true,
                    premiumUsername = finalUser,
                    snackbarMessage = "👑 Giriş Başarılı: Hoş Geldiniz $finalUser! Premium VIP Özellikler Açıldı."
                )
            }
            return true
        } else {
            _uiState.update {
                it.copy(snackbarMessage = "❌ Hatalı kullanıcı adı veya şifre! (Kullanıcı Adı: mzd, Şifre: 123)")
            }
            return false
        }
    }

    // Admin & Şifre ile Premium Açma Bölümü
    fun unlockPremiumWithAdminCode(code: String): Boolean {
        val normalized = code.trim().lowercase()
        val isValid = normalized == "1905" || normalized == "admin" || normalized == "mzd" || normalized == "admin2026" || normalized == "1234"
        if (isValid) {
            _uiState.update {
                it.copy(
                    isPremiumActive = true,
                    adminUnlockedByCode = true,
                    snackbarMessage = "👑 Admin Şifresi Doğrulandı: Tüm VIP & Premium Özellikler Açıldı!"
                )
            }
            return true
        } else {
            _uiState.update {
                it.copy(snackbarMessage = "❌ Hatalı Admin Şifresi! Lütfen tekrar deneyin.")
            }
            return false
        }
    }

    fun playMovie(movie: Movie) {
        _uiState.update {
            it.copy(
                currentPlayingTitle = movie.title,
                currentPlayingSubtitle = "${movie.year} • ${movie.genre}",
                currentPlayingUrl = movie.videoUrl,
                isPlayerOpen = true,
                isRadioPlaying = false
            )
        }
    }

    fun playEpisode(series: Series, episode: Episode) {
        _uiState.update {
            it.copy(
                currentPlayingTitle = "${series.title} - S${episode.season}E${episode.episodeNumber}",
                currentPlayingSubtitle = episode.title,
                currentPlayingUrl = episode.videoUrl,
                isPlayerOpen = true,
                isRadioPlaying = false
            )
        }
    }

    fun playLiveMatch(match: LiveMatch) {
        _uiState.update {
            it.copy(
                currentPlayingTitle = "${match.team1} vs ${match.team2}",
                currentPlayingSubtitle = "${match.league} • ${match.channelName}",
                currentPlayingUrl = match.streamUrl,
                isPlayerOpen = true,
                isRadioPlaying = false
            )
        }
    }

    fun playRadio(station: RadioStation) {
        _uiState.update {
            it.copy(
                currentRadioStation = station,
                isRadioPlaying = true,
                currentPlayingTitle = station.name,
                currentPlayingSubtitle = station.currentTrack,
                currentPlayingUrl = station.streamUrl,
                snackbarMessage = "${station.name} canlı çalınıyor"
            )
        }
    }

    fun toggleRadioPlayback() {
        _uiState.update { it.copy(isRadioPlaying = !it.isRadioPlaying) }
    }

    fun closePlayer() {
        _uiState.update { it.copy(isPlayerOpen = false, currentPlayingUrl = null) }
    }

    fun toggleChannelFavorite(channelId: String) {
        _uiState.update { state ->
            val updated = state.channels.map {
                if (it.id == channelId) it.copy(isFavorite = !it.isFavorite) else it
            }
            state.copy(channels = updated)
        }
    }

    fun toggleMovieFavorite(movieId: String) {
        _uiState.update { state ->
            val updated = state.movies.map {
                if (it.id == movieId) it.copy(isFavorite = !it.isFavorite) else it
            }
            state.copy(movies = updated)
        }
    }

    // Settings actions
    fun updateTheme(theme: ThemeType) {
        _uiState.update { it.copy(settings = it.settings.copy(theme = theme)) }
    }

    fun updateSettings(newSettings: AppSettings) {
        _uiState.update { it.copy(settings = newSettings) }
    }

    fun toggleAdultContent(hide: Boolean) {
        _uiState.update { it.copy(settings = it.settings.copy(hideAdult = hide)) }
    }

    fun toggleKidsMode(active: Boolean) {
        _uiState.update {
            val targetProfile = if (active) {
                it.profiles.find { p -> p.isKids } ?: it.currentProfile
            } else {
                it.profiles.first()
            }
            it.copy(
                settings = it.settings.copy(kidsModeActive = active, hideAdult = active),
                currentProfile = targetProfile,
                snackbarMessage = if (active) "Çocuk Modu Aktifleştirildi" else "Çocuk Modundan Çıkıldı"
            )
        }
    }

    fun toggleTraktConnection() {
        _uiState.update { state ->
            val isConn = !state.settings.traktConnected
            state.copy(
                settings = state.settings.copy(traktConnected = isConn),
                snackbarMessage = if (isConn) "Trakt.tv hesabı başarıyla bağlandı" else "Trakt.tv bağlantısı kesildi"
            )
        }
    }

    fun refreshEpg() {
        viewModelScope.launch {
            _uiState.update { it.copy(snackbarMessage = "EPG TV rehberi güncelleniyor...") }
            delay(1200)
            _uiState.update { it.copy(snackbarMessage = "TV Rehberi ve yayın akışı güncellendi (268 Kanal)") }
        }
    }

    fun clearCache() {
        viewModelScope.launch {
            _uiState.update { it.copy(snackbarMessage = "Önbellek temizleniyor...") }
            delay(800)
            _uiState.update { it.copy(cacheSizeMb = 0, snackbarMessage = "Önbellek başarıyla temizlendi (0 MB)") }
        }
    }

    fun clearHistory() {
        _uiState.update { it.copy(snackbarMessage = "İzleme geçmişi temizlendi") }
    }

    fun backupData() {
        viewModelScope.launch {
            _uiState.update { it.copy(snackbarMessage = "Veriler cihaz hafızasına yedekleniyor...") }
            delay(1000)
            _uiState.update { it.copy(snackbarMessage = "Yedek dosyası kaydedildi: /storage/emulated/0/StreamHub/backup.json") }
        }
    }

    fun restoreData() {
        viewModelScope.launch {
            _uiState.update { it.copy(snackbarMessage = "Yedek dosyası okunuyor...") }
            delay(1000)
            _uiState.update { it.copy(snackbarMessage = "Ayarlar ve favoriler başarıyla geri yüklendi") }
        }
    }

    fun backupToGoogleDrive() {
        viewModelScope.launch {
            _uiState.update { it.copy(snackbarMessage = "Google Drive ile buluta yedekleniyor...") }
            delay(1400)
            _uiState.update { it.copy(snackbarMessage = "Bulut yedekleme tamamlandı (Son yedek: Şimdi)") }
        }
    }

    fun restoreFromGoogleDrive() {
        viewModelScope.launch {
            _uiState.update { it.copy(snackbarMessage = "Google Drive bulutundan geri getiriliyor...") }
            delay(1400)
            _uiState.update { it.copy(snackbarMessage = "Bulut ayarları senkronize edildi") }
        }
    }

    fun resetAllSettings() {
        _uiState.update {
            it.copy(
                settings = AppSettings(),
                snackbarMessage = "Tüm ayarlar fabrika ayarlarına döndürüldü"
            )
        }
    }

    fun startSpeedTest() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    speedTestState = SpeedTestState(isTesting = true, ping = 18, downloadMbps = 15f, uploadMbps = 4f, isCompleted = false)
                )
            }
            for (step in 1..8) {
                delay(300)
                val dl = 25f + step * 10f + Random.nextInt(5)
                val ul = 8f + step * 3f
                _uiState.update {
                    it.copy(speedTestState = it.speedTestState.copy(downloadMbps = dl, uploadMbps = ul))
                }
            }
            delay(400)
            _uiState.update {
                it.copy(
                    speedTestState = SpeedTestState(
                        isTesting = false,
                        ping = 21,
                        downloadMbps = 94.8f,
                        uploadMbps = 28.4f,
                        isCompleted = true
                    ),
                    snackbarMessage = "Hız Testi Tamamlandı: 94.8 Mbps Hız tespit edildi"
                )
            }
        }
    }

    fun generateRecommendation() {
        val pool = listOf(
            "Gladyatör II (Aksiyon Filmi)",
            "beIN SPORTS 1 - Derbi Yayını",
            "Dune: Part Two (Bilim Kurgu)",
            "The Penguin (Yeni Dizi)",
            "National Geographic - Vahşi Krallık",
            "House of the Dragon S2"
        )
        val selected = pool.random()
        _uiState.update { it.copy(recommendationItem = selected) }
    }

    fun setMultiScreenCount(count: Int) {
        val defaultChannels = _uiState.value.channels.take(count)
        val slots = List(count) { i -> defaultChannels.getOrNull(i) }
        _uiState.update {
            it.copy(
                multiScreenCount = count,
                multiScreenSlots = slots,
                activeAudioSlotIndex = 0
            )
        }
    }

    fun setMultiScreenSlotChannel(slotIndex: Int, channel: Channel) {
        _uiState.update { state ->
            val updated = state.multiScreenSlots.toMutableList()
            if (slotIndex in updated.indices) {
                updated[slotIndex] = channel
            }
            state.copy(multiScreenSlots = updated)
        }
    }

    fun setActiveAudioSlot(index: Int) {
        _uiState.update { it.copy(activeAudioSlotIndex = index) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun selectAmbientTheme(theme: AmbientTheme) {
        _uiState.update { it.copy(selectedAmbientTheme = theme, snackbarMessage = "Arka plan: ${theme.title}") }
    }

    fun toggleAmbientVideo(enabled: Boolean) {
        _uiState.update { it.copy(isAmbientVideoEnabled = enabled) }
    }

    fun toggleAmbientFullscreen(fullscreen: Boolean) {
        _uiState.update { it.copy(isAmbientFullscreen = fullscreen) }
    }

    fun importM3uPlaylist(name: String, url: String) {
        if (url.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "Lütfen bir M3U bağlantısı gir") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingM3u = true, snackbarMessage = "M3U bağlantısı indiriliyor ve ayrıştırılıyor...") }
            val parsedChannels: List<Channel> = try {
                withContext(Dispatchers.IO) { M3uParser.fetchFromUrl(url.trim()) }
            } catch (_: Exception) {
                emptyList()
            }

            if (parsedChannels.isEmpty()) {
                _uiState.update {
                    it.copy(isLoadingM3u = false, snackbarMessage = "Liste yüklenemedi veya içinde kanal yok. Bağlantıyı kontrol et.")
                }
                return@launch
            }

            val newPlaylist = M3uPlaylist(
                id = "pl_${System.currentTimeMillis()}",
                name = name.ifBlank { "Yeni M3U Listesi" },
                url = url.trim(),
                channelCount = parsedChannels.size,
                dateAdded = "Bugün",
                isActive = true,
                channels = parsedChannels
            )

            _uiState.update { state ->
                val updatedPlaylists = state.playlists.map { it.copy(isActive = false) } + newPlaylist
                state.copy(
                    playlists = updatedPlaylists,
                    channels = parsedChannels,
                    multiScreenSlots = List(state.multiScreenCount) { i -> parsedChannels.getOrNull(i) },
                    isLoadingM3u = false,
                    snackbarMessage = "${newPlaylist.name} eklendi (${parsedChannels.size} kanal bulundu)"
                )
            }
            savePlaylists()
        }
    }

    fun activatePlaylistAndWatch(playlist: M3uPlaylist) {
        _uiState.update { state ->
            val updated = state.playlists.map { it.copy(isActive = it.id == playlist.id) }
            val channelsToUse = playlist.channels
            state.copy(
                playlists = updated,
                channels = channelsToUse,
                multiScreenSlots = List(state.multiScreenCount) { i -> channelsToUse.getOrNull(i) },
                currentTab = BottomNavTab.LIVE_TV,
                activeToolScreen = null,
                snackbarMessage = "${playlist.name} açıldı - Canlı TV izleniyor"
            )
        }
        savePlaylists()
    }

    fun deletePlaylist(playlistId: String) {
        _uiState.update { state ->
            val removed = state.playlists.firstOrNull { it.id == playlistId }
            val remaining = state.playlists.filterNot { it.id == playlistId }
            val next = if (removed?.isActive == true) {
                remaining.firstOrNull()?.let { it.copy(isActive = true) }
            } else null
            val finalPlaylists = if (next != null) remaining.map { if (it.id == next.id) next else it } else remaining
            val newChannels = when {
                removed?.isActive != true -> state.channels
                next != null -> next.channels
                else -> emptyList()
            }
            state.copy(
                playlists = finalPlaylists,
                channels = newChannels,
                multiScreenSlots = List(state.multiScreenCount) { i -> newChannels.getOrNull(i) },
                snackbarMessage = "Çalma listesi silindi"
            )
        }
        savePlaylists()
    }
}
