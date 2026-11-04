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
    onBack: () -> Unit,
    onSupportClick: () -> Unit
) {
    ToolDetailContainer(title = "Premium Ol - Destek Ol", onBack = onBack) {
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "VIP Premium Kulübü", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Uygulamanın gelişimine katkıda bulunun; tüm VIP renk temalarının, 4'lü çoklu ekranın ve öncelikli akış arabelleğinin kilidini açın.",
                        color = Color(0xFFFEF3C7),
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Premium Avantajları", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(8.dp))

        listOf(
            "Reklamsız ve akıcı deneyim",
            "4'lü Eşzamanlı Çoklu Ekran (Multi-Screen)",
            "Tüm Özel VIP Temalar (Cyberpunk, Gold VIP, AMOLED)",
            "Gelişmiş Teknik İstatistik & Arabellek İyileştirmeleri",
            "Otomatik Trakt.tv Bulut Senkronizasyonu"
        ).forEach { feature ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = feature, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onSupportClick,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Geliştiriciye Destek Ol (Aylık 49,99 ₺)", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

// 2. Account Screen
@Composable
fun AccountScreen(
    accountInfo: AccountInfo,
    onBack: () -> Unit
) {
    ToolDetailContainer(title = "Hesap Yönetimi", onBack = onBack) {
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
                        Text(text = "Aktif", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = MaterialTheme.colorScheme.surface)
                Spacer(modifier = Modifier.height(14.dp))

                AccountDetailRow(label = "Kullanıcı Adı", value = accountInfo.username)
                AccountDetailRow(label = "Son Kullanım Tarihi", value = accountInfo.expiryDate)
                AccountDetailRow(label = "Maksimum Bağlantı", value = "${accountInfo.activeConnections} / ${accountInfo.maxConnections} Aktif Cihaz")
                AccountDetailRow(label = "Sunucu Adresi", value = accountInfo.serverUrl)
                AccountDetailRow(label = "Desteklenen Protokoller", value = accountInfo.serverProtocol)
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
