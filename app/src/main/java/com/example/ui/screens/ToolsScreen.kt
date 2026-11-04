package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.ToolScreen

data class ToolItem(
    val tool: ToolScreen,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val badge: String? = null
)

@Composable
fun ToolsScreen(
    onSelectTool: (ToolScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val toolItems = listOf(
        ToolItem(
            tool = ToolScreen.PREMIUM,
            title = "Premium Ol - Destek Ol",
            description = "Geliştiriciye destek ol ve VIP temaların kilidini aç",
            icon = Icons.Default.WorkspacePremium,
            color = Color(0xFFF59E0B),
            badge = "VIP"
        ),
        ToolItem(
            tool = ToolScreen.ACCOUNT,
            title = "Hesap Yönetimi",
            description = "Abonelik süresi, sunucu ve paket detayları",
            icon = Icons.Default.AccountBox,
            color = Color(0xFF38BDF8)
        ),
        ToolItem(
            tool = ToolScreen.SETTINGS,
            title = "Ayarlar",
            description = "Tema, oynatıcı, ağ, EPG ve sistem yapılandırması",
            icon = Icons.Default.Settings,
            color = Color(0xFF94A3B8)
        ),
        ToolItem(
            tool = ToolScreen.KIDS_MODE,
            title = "Çocuk Modu",
            description = "PIN korumalı güvenli içerik filtreleme",
            icon = Icons.Default.ChildCare,
            color = Color(0xFF10B981)
        ),
        ToolItem(
            tool = ToolScreen.FOLDERS,
            title = "Klasörüm",
            description = "Özel listeler ve favori içerik grupları",
            icon = Icons.Default.Folder,
            color = Color(0xFF818CF8)
        ),
        ToolItem(
            tool = ToolScreen.MULTI_SCREEN,
            title = "Çoklu Ekran",
            description = "2'li veya 4'lü bölünmüş ekranda canlı maç izle",
            icon = Icons.Default.GridView,
            color = Color(0xFFEC4899),
            badge = "2x / 4x"
        ),
        ToolItem(
            tool = ToolScreen.EPG,
            title = "TV Rehberi (EPG)",
            description = "Yayın akışı saatleri ve program detayları",
            icon = Icons.Default.EventNote,
            color = Color(0xFF06B6D4)
        ),
        ToolItem(
            tool = ToolScreen.STATS,
            title = "İstatistik",
            description = "İzleme süreleri ve kullanım alışkanlıkları",
            icon = Icons.Default.BarChart,
            color = Color(0xFF6366F1)
        ),
        ToolItem(
            tool = ToolScreen.WHAT_TO_WATCH,
            title = "Bugün Ne İzlesem?",
            description = "Rastgele veya popülerliğe göre keşif aracı",
            icon = Icons.Default.AutoAwesome,
            color = Color(0xFFA855F7),
            badge = "Keşfet"
        ),
        ToolItem(
            tool = ToolScreen.RECENT_ADDED,
            title = "Son Eklenenler",
            description = "Yeni eklenen filmler, diziler ve kanallar",
            icon = Icons.Default.NotificationsActive,
            color = Color(0xFFEF4444)
        ),
        ToolItem(
            tool = ToolScreen.SPORTS_GUIDE,
            title = "Spor Rehberi",
            description = "Haftalık spor ve maç takvimi rehberi",
            icon = Icons.Default.EmojiEvents,
            color = Color(0xFFF97316)
        ),
        ToolItem(
            tool = ToolScreen.SERIES_CALENDAR,
            title = "Dizi Takvimi",
            description = "Takip ettiğiniz dizilerin yeni bölüm takvimi",
            icon = Icons.Default.CalendarToday,
            color = Color(0xFF14B8A6)
        ),
        ToolItem(
            tool = ToolScreen.SPORTS_RADAR,
            title = "Spor Radarı",
            description = "Canlı maç sonuçları, skorlar ve bildirimler",
            icon = Icons.Default.SportsSoccer,
            color = Color(0xFF22C55E),
            badge = "CANLI"
        ),
        ToolItem(
            tool = ToolScreen.RADIO,
            title = "Radyo",
            description = "Canlı radyo kanallarını dinleme bölümü",
            icon = Icons.Default.Radio,
            color = Color(0xFF8B5CF6)
        ),
        ToolItem(
            tool = ToolScreen.ABOUT,
            title = "Hakkında",
            description = "Sürüm v5.4.18, geliştirici ve destek kanalları",
            icon = Icons.Default.Info,
            color = Color(0xFF64748B)
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "ARAÇLAR MENÜSÜ",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(toolItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectTool(item.tool) }
                        .testTag("tool_card_${item.tool.name.lowercase()}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(item.color.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    item.icon,
                                    contentDescription = item.title,
                                    tint = item.color,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            if (item.badge != null) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = item.color.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = item.badge,
                                        color = item.color,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = item.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = item.description,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 13.sp,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}
