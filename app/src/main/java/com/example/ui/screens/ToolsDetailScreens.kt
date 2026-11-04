package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.model.*

@Composable
fun ToolDetailContainer(
    title: String,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize().statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

// 1. Premium Screen
@Composable
fun PremiumScreen(
    isPremiumActive: Boolean,
    onUnlockWithCode: (String) -> Boolean,
    onBack: () -> Unit,
    onSupportClick: () -> Unit
) {
    var adminCodeInput by remember { mutableStateOf("") }
    var codeError by remember { mutableStateOf(false) }
    var codeSuccess by remember { mutableStateOf(false) }

    ToolDetailContainer(title = "Premium & Admin Yönetimi", onBack = onBack) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF78350F), Color(0xFFD97706), Color(0xFFF59E0B))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "MZD VIP Premium", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0x33000000)
                        ) {
                            Text(
                                text = if (isPremiumActive) "👑 AKTİF (VIP)" else "STANDART",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isPremiumActive)
                            "Hesabınızda VIP Premium özellikler açık durumdadır. Tüm temalar, 4'lü çoklu ekran ve öncelikli akış arabelleği aktif."
                        else
                            "Geliştiriciye destek olarak veya Admin şifresi girerek tüm VIP özelliklerin kilidini açabilirsiniz.",
                        color = Color(0xFFFEF3C7),
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Admin Bölümü (Şifre ile Premium Açma)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Admin Bölümü (Şifre ile Premium Aç)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Yetkili admin şifresini (Ör: 1905 veya admin) girerek bu cihazda ve diğer kullanıcılarda tüm premium özellikleri anında aktifleştirebilirsiniz.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = adminCodeInput,
                        onValueChange = {
                            adminCodeInput = it
                            codeError = false
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Admin Şifresi (Ör: 1905)") },
                        singleLine = true,
                        isError = codeError,
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            val success = onUnlockWithCode(adminCodeInput)
                            if (success) {
                                codeSuccess = true
                                codeError = false
                                adminCodeInput = ""
                            } else {
                                codeError = true
                                codeSuccess = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Doğrula", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }

                if (codeSuccess) {
                    Text(
                        text = "✓ Tebrikler! Admin onayıyla tüm VIP özellikler başarıyla açıldı.",
                        color = Color(0xFF10B981),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
                if (codeError) {
                    Text(
                        text = "❌ Hatalı şifre. Lütfen 1905 veya admin deneyin.",
                        color = Color(0xFFEF4444),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Açık Olan VIP Özellikler", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(6.dp))

        listOf(
            "4'lü Eşzamanlı Çoklu Ekran (Multi-Screen) İzleme",
            "Sınırsız M3U / M3U8 Çalma Listesi İçe Aktarma ve Oynatma",
            "Tüm Özel VIP Temalar (Cyberpunk, Gold VIP, AMOLED Gece)",
            "Kumanda ile Hızlı Kanal Değiştirme (D-Pad)",
            "Radyoda Mevsimler & Şömine Canlı Ambiyans Videoları"
        ).forEach { feature ->
            Row(
                modifier = Modifier.padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = feature, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

// 2. Account Screen (Hesap Yönetimi & M3U/M3U8 Listeleri)
@Composable
fun AccountScreen(
    accountInfo: AccountInfo,
    playlists: List<M3uPlaylist>,
    isLoadingM3u: Boolean,
    onAddPlaylist: (name: String, url: String) -> Unit,
    onActivateAndWatch: (M3uPlaylist) -> Unit,
    onDeletePlaylist: (String) -> Unit,
    onBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var inputName by remember { mutableStateOf("") }
    var inputUrl by remember { mutableStateOf("") }

    ToolDetailContainer(title = "Hesap & M3U / M3U8 Yönetimi", onBack = onBack) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Subscription Details Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Hesap Durumu", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = accountInfo.status, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0x3310B981)
                            ) {
                                Text(text = "Aktif Paket", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = MaterialTheme.colorScheme.surface)
                        Spacer(modifier = Modifier.height(12.dp))

                        AccountDetailRow(label = "Kullanıcı Adı", value = accountInfo.username)
                        AccountDetailRow(label = "Son Kullanım Tarihi", value = accountInfo.expiryDate)
                        AccountDetailRow(label = "Maksimum Bağlantı", value = "${accountInfo.activeConnections} / ${accountInfo.maxConnections} Aktif Cihaz")
                        AccountDetailRow(label = "Sunucu Protokolü", value = accountInfo.serverProtocol)
                    }
                }
            }

            // M3U / M3U8 Add Section
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.PlaylistAdd, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = "M3U / M3U8 Bağlantısı Ekle", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }

                            Button(
                                onClick = { showAddDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Yeni Ekle", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Kendi IPTV sağlayıcınızın .m3u veya .m3u8 linkini ekleyin, otomatik ayrıştırılsın ve anında canlı izlemeye başlayın.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Saved Playlists List
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "KAYITLI ÇALMA LİSTELERİ (${playlists.size})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            items(playlists) { pl ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (pl.isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(14.dp),
                    border = if (pl.isActive) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    Icons.Default.LiveTv,
                                    contentDescription = null,
                                    tint = if (pl.isActive) MaterialTheme.colorScheme.primary else Color.Gray,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = pl.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${pl.channelCount} Kanal • ${pl.dateAdded}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (pl.isActive) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primary
                                ) {
                                    Text(
                                        text = "AKTİF LİSTE",
                                        color = Color.Black,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            } else {
                                IconButton(
                                    onClick = { onDeletePlaylist(pl.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Sil", tint = Color.Gray, modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = pl.url,
                            fontSize = 10.sp,
                            color = Color.Gray,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // "Listeyi Aç ve İzle" Primary Action Button
                        Button(
                            onClick = { onActivateAndWatch(pl) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (pl.isActive) MaterialTheme.colorScheme.primary else Color(0xFF1E293B)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                Icons.Default.PlayCircle,
                                contentDescription = null,
                                tint = if (pl.isActive) Color.Black else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Listeyi Aç ve İzle",
                                color = if (pl.isActive) Color.Black else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Add M3U Dialog
    if (showAddDialog) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showAddDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "M3U / M3U8 Bağlantısı Ekle",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "IPTV servis sağlayıcınızın m3u veya m3u8 linkini girin",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = inputName,
                        onValueChange = { inputName = it },
                        label = { Text("Liste Adı (Örn: Spor & Ulusal)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = inputUrl,
                        onValueChange = { inputUrl = it },
                        label = { Text("M3U / M3U8 URL Adresi") },
                        placeholder = { Text("https://domain.com/playlist.m3u8") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Hızlı Hazır Şablonlar:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SuggestionChip(
                            onClick = {
                                inputName = "Hızlı Test Listesi (M3U8)"
                                inputUrl = "https://stream.mzd.tv/test_iptv.m3u8"
                            },
                            label = { Text("⚡ Test M3U8", fontSize = 10.sp) }
                        )
                        SuggestionChip(
                            onClick = {
                                inputName = "Türkiye Ulusal & Spor"
                                inputUrl = "https://stream.mzd.tv/turkey_channels.m3u"
                            },
                            label = { Text("🇹🇷 TR Spor", fontSize = 10.sp) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showAddDialog = false }) {
                            Text("İptal")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                showAddDialog = false
                                onAddPlaylist(inputName, inputUrl)
                                inputName = ""
                                inputUrl = ""
                            },
                            enabled = !isLoadingM3u,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("İndir & Listeyi Yükle", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}

// 4. Kids Mode Screen
@Composable
fun KidsModeScreen(
    isKidsActive: Boolean,
    onToggleKids: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    ToolDetailContainer(title = "Çocuk Modu", onBack = onBack) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Güvenli Çocuk Profili", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(
                            text = "Yetişkin (+18), şiddet ve korku içerikli kanalları otomatik olarak kilitler ve gizler.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(checked = isKidsActive, onCheckedChange = onToggleKids)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Çocuk Modunda İzin Verilen Kategoriler", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        listOf(
            "Çizgi Dizi ve Animasyon Kanalları (Cartoon Network, Disney)",
            "Eğitici Belgeseller ve Hayvanlar Alemi",
            "Aile & Çocuk Sinema Kuşağı",
            "Müzik & Masal Radyoları"
        ).forEach { item ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

// 5. My Folders Screen
@Composable
fun FoldersScreen(
    folders: List<CustomFolder>,
    onBack: () -> Unit
) {
    ToolDetailContainer(title = "Klasörüm & Özel Listeler", onBack = onBack) {
        Text(
            text = "Kendi oluşturduğunuz özel kategori ve favori içerik klasörleriniz:",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(folders) { folder ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Folder, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = folder.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = "${folder.channelIds.size} Kayıtlı İçerik", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                    }
                }
            }
        }
    }
}

// 7. TV Guide (EPG) Screen
@Composable
fun EpgScreen(
    channels: List<Channel>,
    onRefreshEpg: () -> Unit,
    onBack: () -> Unit
) {
    ToolDetailContainer(title = "TV Rehberi (EPG)", onBack = onBack) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Günlük Yayın Akış Çizelgesi", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = onRefreshEpg) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Akışı Yenile", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(channels) { ch ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = ch.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = ch.category, fontSize = 10.sp, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "• 20:00 - 21:45: ${ch.currentEpg}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        LinearProgressIndicator(
                            progress = { 0.65f },
                            modifier = Modifier.fillMaxWidth().height(4.dp).padding(vertical = 2.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(text = "• 21:45 - 23:30: ${ch.nextEpg}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

// 8. Statistics Screen
@Composable
fun StatsScreen(
    todayMin: Int,
    weekHours: Float,
    onBack: () -> Unit
) {
    ToolDetailContainer(title = "İzleme İstatistikleri", onBack = onBack) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "Bugün", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "$todayMin dk", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(text = "3 kanal izlendi", fontSize = 10.sp, color = Color.Gray)
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "Bu Hafta", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "$weekHours saat", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                    Text(text = "En çok: Spor & Sinema", fontSize = 10.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "En Çok İzlenen Kanallar", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        listOf(
            "beIN SPORTS 1 HD" to 45,
            "Sinema TV Aksiyon" to 25,
            "National Geographic" to 18,
            "S Sport 1 HD" to 12
        ).forEach { (ch, pct) ->
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = ch, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "%$pct", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                }
                LinearProgressIndicator(
                    progress = { pct / 100f },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// 9. What To Watch Roulette Screen
@Composable
fun WhatToWatchScreen(
    recommendation: String?,
    onGenerate: () -> Unit,
    onBack: () -> Unit
) {
    ToolDetailContainer(title = "Bugün Ne İzlesem?", onBack = onBack) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Ne izleyeceğinize karar veremediniz mi?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Yapay zeka ve popülerlik algoritması sizin için en iyi içeriği seçsin.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )

            if (recommendation != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "SİZİN İÇİN ÖNERİMİZ", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = recommendation, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onGenerate,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Casino, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Rastgele İçerik Öner", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 10. Recent Added Screen
@Composable
fun RecentAddedScreen(
    movies: List<Movie>,
    series: List<Series>,
    onBack: () -> Unit
) {
    ToolDetailContainer(title = "Son Eklenenler", onBack = onBack) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                Text(text = "En Yeni Filmler", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
            }
            items(movies.filter { it.isNew }) { m ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.NewReleases, contentDescription = null, tint = Color(0xFFEF4444))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = m.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "${m.year} • ${m.genre}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Yeni Eklenen Diziler", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)
            }
            items(series.filter { it.isNew }) { s ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FiberNew, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = s.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "${s.category} • ${s.totalSeasons} Sezon", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

// 11. Sports Guide Screen
@Composable
fun SportsGuideScreen(onBack: () -> Unit) {
    ToolDetailContainer(title = "Spor Rehberi (Maç Takvimi)", onBack = onBack) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(SampleData.sampleLiveMatches) { match ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = match.league, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            Text(text = "${match.team1} vs ${match.team2}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = "Yayıncı: ${match.channelName}", fontSize = 11.sp, color = Color.Gray)
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0x33F59E0B)
                        ) {
                            Text(text = match.matchTime, color = Color(0xFFF59E0B), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                }
            }
        }
    }
}

// 12. Series Calendar Screen
@Composable
fun SeriesCalendarScreen(onBack: () -> Unit) {
    ToolDetailContainer(title = "Dizi Takvimi", onBack = onBack) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(SampleData.sampleSeriesCalendar) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item.seriesTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = item.episodeTitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            Text(text = "Yayın: ${item.channel}", fontSize = 10.sp, color = Color.Gray)
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0x3310B981)
                        ) {
                            Text(text = "${item.dayName} ${item.time}", color = Color(0xFF10B981), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                }
            }
        }
    }
}

// 13. Sports Radar Screen
@Composable
fun SportsRadarScreen(onBack: () -> Unit) {
    ToolDetailContainer(title = "Spor Radarı (Canlı Skorlar)", onBack = onBack) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(SampleData.sampleSportsRadar) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = item.league, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            Text(text = item.minute, fontSize = 11.sp, color = if (item.status == "Canlı") Color(0xFFEF4444) else Color.Gray, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${item.homeTeam} - ${item.awayTeam}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(text = "${item.homeScore} : ${item.awayScore}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.lastAction, fontSize = 11.sp, color = Color(0xFFCBD5E1))
                    }
                }
            }
        }
    }
}

// 15. About Screen
@Composable
fun AboutScreen(onBack: () -> Unit) {
    ToolDetailContainer(title = "Hakkında", onBack = onBack) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Tv, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "MZD TV Box & Mobile Media Suite", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                Text(text = "Sürüm: v5.4.18 (Build 2026.11)", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "MZD; Android TV Box cihazları, akıllı televizyonlar ve Android telefonlar için özel olarak optimize edilmiş, çoklu ekran, canlı EPG, ambiyans arka plan videolu radyo ve akıllı arabellek destekli profesyonel medya izleme platformudur.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Topluluk ve İletişim Kanalları", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        listOf(
            "Telegram Resmi Kanalı: @MZDCommunity",
            "YouTube Rehber & Güncellemeler: @MZDOfficial",
            "E-Posta Desteği: support@mzd.tv",
            "Açık Kaynak Lisansları: MIT / Apache 2.0"
        ).forEach { info ->
            Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Share, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = info, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
