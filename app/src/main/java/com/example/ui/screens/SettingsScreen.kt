package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.viewmodel.SpeedTestState

enum class SettingsSubMenu {
    MAIN,
    PROFILE_CONTENT,
    HOME_SCREEN,
    APPEARANCE_THEMES,
    PLAYER_SUBTITLES,
    NETWORK_DNS,
    SECURITY_PRIVACY,
    SYSTEM_DATA
}

@Composable
fun SettingsScreen(
    settings: AppSettings,
    cacheSizeMb: Int,
    speedTestState: SpeedTestState,
    onUpdateSettings: (AppSettings) -> Unit,
    onClearCache: () -> Unit,
    onClearHistory: () -> Unit,
    onBackupData: () -> Unit,
    onRestoreData: () -> Unit,
    onBackupGoogle: () -> Unit,
    onRestoreGoogle: () -> Unit,
    onResetAll: () -> Unit,
    onStartSpeedTest: () -> Unit,
    onRefreshEpg: () -> Unit,
    onToggleTrakt: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentSubMenu by remember { mutableStateOf(SettingsSubMenu.MAIN) }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize().statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (currentSubMenu != SettingsSubMenu.MAIN) {
                            currentSubMenu = SettingsSubMenu.MAIN
                        } else {
                            onBack()
                        }
                    }
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = when (currentSubMenu) {
                            SettingsSubMenu.MAIN -> "Ayarlar"
                            SettingsSubMenu.PROFILE_CONTENT -> "Profil & İçerik Ayarları"
                            SettingsSubMenu.HOME_SCREEN -> "Ana Sayfa Ayarları"
                            SettingsSubMenu.APPEARANCE_THEMES -> "Görünüm & Premium Temalar"
                            SettingsSubMenu.PLAYER_SUBTITLES -> "Oynatıcı & Alt Yazı Ayarları"
                            SettingsSubMenu.NETWORK_DNS -> "Ağ, Arabellek & DNS Ayarları"
                            SettingsSubMenu.SECURITY_PRIVACY -> "Güvenlik & Gizlilik"
                            SettingsSubMenu.SYSTEM_DATA -> "Sistem & Veri Yönetimi"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "StreamHub v5.4.18 Yapılandırması",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            when (currentSubMenu) {
                SettingsSubMenu.MAIN -> {
                    MainMenuContent(onNavigate = { currentSubMenu = it })
                }
                SettingsSubMenu.PROFILE_CONTENT -> {
                    ProfileContentSettings(settings, onUpdateSettings, onToggleTrakt)
                }
                SettingsSubMenu.HOME_SCREEN -> {
                    HomeScreenSettings(settings, onUpdateSettings)
                }
                SettingsSubMenu.APPEARANCE_THEMES -> {
                    AppearanceThemesSettings(settings, onUpdateSettings)
                }
                SettingsSubMenu.PLAYER_SUBTITLES -> {
                    PlayerSubtitlesSettings(settings, onUpdateSettings, onRefreshEpg)
                }
                SettingsSubMenu.NETWORK_DNS -> {
                    NetworkDnsSettings(settings, onUpdateSettings)
                }
                SettingsSubMenu.SECURITY_PRIVACY -> {
                    SecurityPrivacySettings(settings, onUpdateSettings)
                }
                SettingsSubMenu.SYSTEM_DATA -> {
                    SystemDataSettings(
                        settings = settings,
                        cacheSizeMb = cacheSizeMb,
                        speedTestState = speedTestState,
                        onUpdateSettings = onUpdateSettings,
                        onClearCache = onClearCache,
                        onClearHistory = onClearHistory,
                        onBackupData = onBackupData,
                        onRestoreData = onRestoreData,
                        onBackupGoogle = onBackupGoogle,
                        onRestoreGoogle = onRestoreGoogle,
                        onResetAll = onResetAll,
                        onStartSpeedTest = onStartSpeedTest
                    )
                }
            }
        }
    }
}

// Main Settings Category Cards
@Composable
private fun MainMenuContent(onNavigate: (SettingsSubMenu) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SettingsCategoryCard(
                title = "Profil & İçerik Ayarları",
                subtitle = "Kategori gizle/kilitle, türler menüsü, Trakt.tv ve yetişkin filtresi",
                icon = Icons.Default.ManageAccounts,
                accentColor = Color(0xFF38BDF8),
                onClick = { onNavigate(SettingsSubMenu.PROFILE_CONTENT) }
            )
        }
        item {
            SettingsCategoryCard(
                title = "Ana Sayfa",
                subtitle = "İçerik satırlarını açıp kapatma ve sıralama",
                icon = Icons.Default.Home,
                accentColor = Color(0xFF818CF8),
                onClick = { onNavigate(SettingsSubMenu.HOME_SCREEN) }
            )
        }
        item {
            SettingsCategoryCard(
                title = "Görünüm & Premium Temalar",
                subtitle = "AMOLED Siyah, Modern Koyu, Okyanus, Siberpunk ve VIP temalar",
                icon = Icons.Default.Palette,
                accentColor = Color(0xFFEC4899),
                onClick = { onNavigate(SettingsSubMenu.APPEARANCE_THEMES) }
            )
        }
        item {
            SettingsCategoryCard(
                title = "Oynatıcı & Alt Yazı Ayarları",
                subtitle = "Kategori sütunları, HW hızlandırma, PiP modu, altyazı stili ve EPG",
                icon = Icons.Default.PlayCircle,
                accentColor = Color(0xFF10B981),
                onClick = { onNavigate(SettingsSubMenu.PLAYER_SUBTITLES) }
            )
        }
        item {
            SettingsCategoryCard(
                title = "Ağ, Arabellek & DNS Sağlayıcısı",
                subtitle = "Akıllı arabellek modu ve İSS engeli aşma DNS ayarları",
                icon = Icons.Default.Dns,
                accentColor = Color(0xFFF59E0B),
                onClick = { onNavigate(SettingsSubMenu.NETWORK_DNS) }
            )
        }
        item {
            SettingsCategoryCard(
                title = "Güvenlik & Gizlilik",
                subtitle = "Ebeveyn kilidi ve şifreleme",
                icon = Icons.Default.Security,
                accentColor = Color(0xFFEF4444),
                onClick = { onNavigate(SettingsSubMenu.SECURITY_PRIVACY) }
            )
        }
        item {
            SettingsCategoryCard(
                title = "Sistem & Veri Yönetimi",
                subtitle = "İnternet hız testi, önbellek temizleme, yedekleme ve sıfırlama",
                icon = Icons.Default.Storage,
                accentColor = Color(0xFFA855F7),
                onClick = { onNavigate(SettingsSubMenu.SYSTEM_DATA) }
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "StreamHub v5.4.18 (Resmi Stabil Sürüm)",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// 1. Profil & İçerik Ayarları
@Composable
private fun ProfileContentSettings(
    settings: AppSettings,
    onUpdate: (AppSettings) -> Unit,
    onToggleTrakt: () -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            SettingHeader(title = "KATEGORİ VE İÇERİK KONTROLÜ")
        }
        item {
            SettingRowAction(
                title = "Kategori Yönetimi (Gizle / Kilitle / Sırala)",
                subtitle = "Canlı TV, film veya dizi kategorilerini özelleştirin",
                icon = Icons.Default.Reorder,
                onClick = {}
            )
        }
        item {
            SettingSwitchRow(
                title = "Türler Menüsünü Göster",
                subtitle = "İçerik türlerinin (Aksiyon, Komedi vb.) ayrı sekme olarak görünmesi",
                icon = Icons.Default.Category,
                checked = settings.showGenresMenu,
                onCheckedChange = { onUpdate(settings.copy(showGenresMenu = it)) }
            )
        }
        item {
            SettingRowAction(
                title = "Trakt Hesabını Bağla",
                subtitle = if (settings.traktConnected) "Bağlı (${settings.traktUsername}) - Kaldığın yeri eşitle" else "Hesap bağlı değil - İzleme geçmişini eşitle",
                icon = Icons.Default.Sync,
                onClick = onToggleTrakt
            )
        }
        item {
            SettingRowAction(
                title = "Çocuk Modu İçerik Seçimi",
                subtitle = "Çocuk profilinde gösterilecek güvenli içerikleri belirleyin",
                icon = Icons.Default.ChildCare,
                onClick = {}
            )
        }
        item {
            SettingSwitchRow(
                title = "Yetişkin Yayınları Gizle (+18)",
                subtitle = "Yetişkin içerikli yayın ve kanalları liste dışı bırakır",
                icon = Icons.Default.Lock,
                checked = settings.hideAdult,
                onCheckedChange = { onUpdate(settings.copy(hideAdult = it)) }
            )
        }
        item {
            SettingSwitchRow(
                title = "Gelişmiş İstatistikler",
                subtitle = "Çözünürlük, codec, FPS ve bağlantı hızını oynatıcıda göster",
                icon = Icons.Default.Speed,
                checked = settings.advancedPlayerStats,
                onCheckedChange = { onUpdate(settings.copy(advancedPlayerStats = it)) }
            )
        }
    }
}

// 2. Ana Sayfa Ayarları
@Composable
private fun HomeScreenSettings(
    settings: AppSettings,
    onUpdate: (AppSettings) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            SettingHeader(title = "ANA SAYFA İÇERİK SATIRLARI")
        }
        listOf(
            "Bugün Canlıda (Önemli Maçlar Banner)",
            "Hızlı Kategori Kısayolları (TV, Film, Dizi, Radyo)",
            "Son İzlenenler Satırı",
            "Popüler Canlı TV Kanalları",
            "Trend Filmler ve Diziler"
        ).forEach { rowTitle ->
            item {
                SettingSwitchRow(
                    title = rowTitle,
                    subtitle = "Ana sayfa ekranında aktif olarak göster",
                    icon = Icons.Default.ViewAgenda,
                    checked = true,
                    onCheckedChange = {}
                )
            }
        }
    }
}

// 3. Görünüm & Premium Temalar
@Composable
private fun AppearanceThemesSettings(
    settings: AppSettings,
    onUpdate: (AppSettings) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            SettingHeader(title = "UYGULAMA RENK VE TEMALARI")
        }
        ThemeType.values().forEach { themeType ->
            val isSelected = settings.theme == themeType
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onUpdate(settings.copy(theme = themeType)) },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = themeType.displayName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = themeType.description,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        RadioButton(
                            selected = isSelected,
                            onClick = { onUpdate(settings.copy(theme = themeType)) }
                        )
                    }
                }
            }
        }
    }
}

// 4. Oynatıcı & Alt Yazı Ayarları
@Composable
private fun PlayerSubtitlesSettings(
    settings: AppSettings,
    onUpdate: (AppSettings) -> Unit,
    onRefreshEpg: () -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { SettingHeader(title = "GÖRÜNÜM VE OYNATICI DÜZENİ") }
        item {
            SettingOptionPicker(
                title = "Kategori Sütun",
                subtitle = "Listelemenin kaç sütun halinde görüneceğini belirleyin",
                currentValue = "${settings.playerColumns} Sütun",
                options = listOf("1 Sütun", "2 Sütun"),
                onSelect = {
                    val cols = if (it.startsWith("1")) 1 else 2
                    onUpdate(settings.copy(playerColumns = cols))
                }
            )
        }
        item {
            SettingSwitchRow(
                title = "Film & Dizi Lite Görünüm",
                subtitle = "Listeleri daha sade ve hafif bir biçimde gösterir",
                icon = Icons.Default.ViewList,
                checked = settings.liteView,
                onCheckedChange = { onUpdate(settings.copy(liteView = it)) }
            )
        }
        item {
            SettingOptionPicker(
                title = "Altyazı Stili ve Ayarları",
                subtitle = "Yazı boyutu ve görünümü",
                currentValue = "${settings.subtitleSize.toInt()} pt • ${settings.subtitleColor}",
                options = listOf("Küçük (14 pt)", "Orta (16 pt)", "Büyük (20 pt)"),
                onSelect = {
                    val size = when {
                        it.contains("14") -> 14f
                        it.contains("20") -> 20f
                        else -> 16f
                    }
                    onUpdate(settings.copy(subtitleSize = size))
                }
            )
        }
        item {
            SettingSwitchRow(
                title = "Donanım Hızlandırma (HW Accel)",
                subtitle = "GPU hızlandırma ile takılmaları önler ve akıcılığı artırır",
                icon = Icons.Default.Memory,
                checked = settings.hwAcceleration,
                onCheckedChange = { onUpdate(settings.copy(hwAcceleration = it)) }
            )
        }
        item {
            SettingSwitchRow(
                title = "Otomatik Küçük Ekran (PiP)",
                subtitle = "Uygulama dışına çıkıldığında videoyu köşede oynatmaya devam et",
                icon = Icons.Default.PictureInPicture,
                checked = settings.autoPiP,
                onCheckedChange = { onUpdate(settings.copy(autoPiP = it)) }
            )
        }
        item {
            SettingSwitchRow(
                title = "Yatay Kullanıma İzin Ver",
                subtitle = "Ekran yön kilidinden bağımsız yatay modda çalışma",
                icon = Icons.Default.ScreenRotation,
                checked = settings.allowLandscape,
                onCheckedChange = { onUpdate(settings.copy(allowLandscape = it)) }
            )
        }
        item {
            SettingOptionPicker(
                title = "Oynatıcıda Varsayılan Yön Kilidi",
                subtitle = "Video oynatılırken yön davranışını belirleyin",
                currentValue = settings.orientationLock,
                options = listOf("Sensöre Göre Otomatik", "Her Zaman Yatay", "Her Zaman Dikey"),
                onSelect = { onUpdate(settings.copy(orientationLock = it)) }
            )
        }

        item { SettingHeader(title = "TV REHBERİ (EPG) VE AKIŞ AYARLARI") }
        item {
            SettingRowAction(
                title = "TV Rehberi (EPG) - Özel XMLTV Linki",
                subtitle = settings.customEpgUrl,
                icon = Icons.Default.Link,
                onClick = {}
            )
        }
        item {
            SettingOptionPicker(
                title = "Saat Kayması (Time Shift)",
                subtitle = "Yayın akışı saatlerini senkronize edin",
                currentValue = "${settings.timeShiftHours} Saat",
                options = listOf("-2 Saat", "-1 Saat", "0 Saat", "+1 Saat", "+2 Saat"),
                onSelect = {
                    val h = when {
                        it.startsWith("-2") -> -2
                        it.startsWith("-1") -> -1
                        it.startsWith("+1") -> 1
                        it.startsWith("+2") -> 2
                        else -> 0
                    }
                    onUpdate(settings.copy(timeShiftHours = h))
                }
            )
        }
        item {
            SettingRowAction(
                title = "Yayın Akışını Şimdi Yenile",
                subtitle = "TV rehberini ve akış verilerini anında günceller",
                icon = Icons.Default.Refresh,
                onClick = onRefreshEpg
            )
        }
        item {
            SettingSwitchRow(
                title = "Kaydırarak Kanal Değiştir (Canlı TV)",
                subtitle = "Ekranda parmağınızı yukarı/aşağı kaydırarak hızlı kanal değiştirme",
                icon = Icons.Default.SwipeVertical,
                checked = settings.swipeChannelSwitch,
                onCheckedChange = { onUpdate(settings.copy(swipeChannelSwitch = it)) }
            )
        }
    }
}

// 5. Ağ, Arabellek & DNS Ayarları
@Composable
private fun NetworkDnsSettings(
    settings: AppSettings,
    onUpdate: (AppSettings) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { SettingHeader(title = "AKILLI AĞ VE ÖNBELLEK") }
        BufferMode.values().forEach { mode ->
            val isSelected = settings.bufferMode == mode
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth().clickable { onUpdate(settings.copy(bufferMode = mode)) }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = mode.displayName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "Arabellek hedefi: ${mode.bufferSeconds / 1000.0} saniye", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        RadioButton(selected = isSelected, onClick = { onUpdate(settings.copy(bufferMode = mode)) })
                    }
                }
            }
        }

        item { SettingHeader(title = "DNS SAĞLAYICISI (İSS ENGELİ AŞMA)") }
        DnsProvider.values().forEach { dns ->
            val isSelected = settings.dnsProvider == dns
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth().clickable { onUpdate(settings.copy(dnsProvider = dns)) }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = dns.displayName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "IP: ${dns.ip}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        RadioButton(selected = isSelected, onClick = { onUpdate(settings.copy(dnsProvider = dns)) })
                    }
                }
            }
        }
    }
}

// 6. Güvenlik & Gizlilik
@Composable
private fun SecurityPrivacySettings(
    settings: AppSettings,
    onUpdate: (AppSettings) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { SettingHeader(title = "EBEVEYN KİLİDİ VE GÜVENLİK") }
        item {
            SettingRowAction(
                title = "Ebeveyn PIN Kodunu Değiştir",
                subtitle = "Mevcut PIN: **** (Varsayılan 0000)",
                icon = Icons.Default.Pin,
                onClick = {}
            )
        }
        item {
            SettingSwitchRow(
                title = "Yetişkin Kategorilerini Şifrele",
                subtitle = "Kategori açılırken PIN kodu sorulsun",
                icon = Icons.Default.Lock,
                checked = true,
                onCheckedChange = {}
            )
        }
        item {
            SettingSwitchRow(
                title = "Çocuk Modundan Çıkışta PIN İste",
                subtitle = "Çocuk profilinden çıkarken güvenlik doğrulaması yap",
                icon = Icons.Default.Security,
                checked = true,
                onCheckedChange = {}
            )
        }
    }
}

// 7. Sistem & Veri Yönetimi
@Composable
private fun SystemDataSettings(
    settings: AppSettings,
    cacheSizeMb: Int,
    speedTestState: SpeedTestState,
    onUpdateSettings: (AppSettings) -> Unit,
    onClearCache: () -> Unit,
    onClearHistory: () -> Unit,
    onBackupData: () -> Unit,
    onRestoreData: () -> Unit,
    onBackupGoogle: () -> Unit,
    onRestoreGoogle: () -> Unit,
    onResetAll: () -> Unit,
    onStartSpeedTest: () -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { SettingHeader(title = "GENEL VE BAŞLANGIÇ AYARLARI") }
        item {
            SettingOptionPicker(
                title = "Uygulama Dili",
                subtitle = "Arayüz dilini değiştirin",
                currentValue = settings.appLanguage,
                options = listOf("Türkçe", "English", "Deutsch"),
                onSelect = { onUpdateSettings(settings.copy(appLanguage = it)) }
            )
        }
        item {
            SettingSwitchRow(
                title = "Açılış Videosu",
                subtitle = "Uygulama açılırken intro animasyonunu göster",
                icon = Icons.Default.PlayCircle,
                checked = settings.splashVideo,
                onCheckedChange = { onUpdateSettings(settings.copy(splashVideo = it)) }
            )
        }
        item {
            SettingOptionPicker(
                title = "Uygulama Başlangıç Ekranı",
                subtitle = "Uygulama açıldığında karşılanacak ekran",
                currentValue = settings.defaultStartScreen,
                options = listOf("Ana Sayfa", "Canlı TV", "Filmler", "Diziler", "Radyo"),
                onSelect = { onUpdateSettings(settings.copy(defaultStartScreen = it)) }
            )
        }

        item { SettingHeader(title = "İNTERNET HIZ TESTİ") }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("speed_test_card")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Bağlantı Kalite Ölçümü", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = "Canlı yayınlar için internet hızınızı test edin", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Button(
                            onClick = onStartSpeedTest,
                            enabled = !speedTestState.isTesting,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = if (speedTestState.isTesting) "Test Ediliyor..." else "Testi Başlat",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    if (speedTestState.isTesting || speedTestState.isCompleted) {
                        Spacer(modifier = Modifier.height(12.dp))
                        if (speedTestState.isTesting) {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth().height(4.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Ping", fontSize = 10.sp, color = Color.Gray)
                                Text(text = "${speedTestState.ping} ms", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "İndirme (Download)", fontSize = 10.sp, color = Color.Gray)
                                Text(text = "${speedTestState.downloadMbps} Mbps", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Yükleme (Upload)", fontSize = 10.sp, color = Color.Gray)
                                Text(text = "${speedTestState.uploadMbps} Mbps", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF818CF8))
                            }
                        }
                    }
                }
            }
        }

        item { SettingHeader(title = "BAKIM VE DEPOLAMA") }
        item {
            SettingRowAction(
                title = "Önbelleği Temizle",
                subtitle = "Biriken geçici dosyaları siler (Şu an: $cacheSizeMb MB)",
                icon = Icons.Default.CleaningServices,
                onClick = onClearCache
            )
        }
        item {
            SettingRowAction(
                title = "İzleme Geçmişini Temizle",
                subtitle = "İzlenen dizi, film ve kanal geçmişini siler",
                icon = Icons.Default.History,
                onClick = onClearHistory
            )
        }

        item { SettingHeader(title = "YEDEKLEME VE BULUT SENKRONİZASYONU") }
        item {
            SettingRowAction(
                title = "Verileri Yedekle (Dışa Aktar)",
                subtitle = "Ayarlarınızı ve favorilerinizi cihaz depolama alanına yedekleyin",
                icon = Icons.Default.FileDownload,
                onClick = onBackupData
            )
        }
        item {
            SettingRowAction(
                title = "Verileri Geri Yükle (İçe Aktar)",
                subtitle = "Cihazdaki yedek dosyasından ayarları yükleyin",
                icon = Icons.Default.FileUpload,
                onClick = onRestoreData
            )
        }
        item {
            SettingRowAction(
                title = "Google ile Buluta Yedekle",
                subtitle = "Ayarlarınızı güvenli Google Drive alanına kaydeder",
                icon = Icons.Default.CloudUpload,
                onClick = onBackupGoogle
            )
        }
        item {
            SettingRowAction(
                title = "Google'dan Geri Yükle",
                subtitle = "Bulutta kayıtlı ayarları geri getirin",
                icon = Icons.Default.CloudDownload,
                onClick = onRestoreGoogle
            )
        }

        item { SettingHeader(title = "FABRİKA AYARLARI") }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0x33EF4444)),
                modifier = Modifier.fillMaxWidth().clickable(onClick = onResetAll)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DeleteForever, contentDescription = null, tint = Color(0xFFEF4444))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Her Şeyi Sil (Fabrika Ayarlarına Dön)", fontWeight = FontWeight.Bold, color = Color(0xFFEF4444), fontSize = 13.sp)
                        Text(text = "Uygulama ayarlarını ve verilerini tamamen sıfırlar", fontSize = 10.sp, color = Color(0xFFFCA5A5))
                    }
                }
            }
        }
    }
}

// Reusable Settings Components
@Composable
private fun SettingHeader(title: String) {
    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
    )
}

@Composable
private fun SettingsCategoryCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

@Composable
private fun SettingRowAction(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(text = subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
private fun SettingOptionPicker(
    title: String,
    subtitle: String,
    currentValue: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth().clickable { expanded = true }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(text = subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            ) {
                Text(
                    text = currentValue,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { opt ->
                    DropdownMenuItem(
                        text = { Text(opt) },
                        onClick = {
                            onSelect(opt)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
