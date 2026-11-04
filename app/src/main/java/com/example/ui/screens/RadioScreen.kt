package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AmbientTheme
import com.example.model.RadioStation
import com.example.ui.components.AmbientBackgroundLayer
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RadioScreen(
    radios: List<RadioStation>,
    isPlaying: Boolean,
    currentStation: RadioStation?,
    ambientThemes: List<AmbientTheme>,
    selectedAmbientTheme: AmbientTheme,
    isAmbientEnabled: Boolean,
    onSelectAmbientTheme: (AmbientTheme) -> Unit,
    onToggleAmbientEnabled: (Boolean) -> Unit,
    onPlayStation: (RadioStation) -> Unit,
    onTogglePlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isTvCinematicMode by remember { mutableStateOf(false) }

    // If TV Cinematic Mode is enabled, render full screen relaxing ambient visuals with minimal HUD
    if (isTvCinematicMode) {
        TvCinematicAmbientView(
            theme = selectedAmbientTheme,
            station = currentStation,
            isPlaying = isPlaying,
            themes = ambientThemes,
            onSelectTheme = onSelectAmbientTheme,
            onTogglePlay = onTogglePlay,
            onExitTvMode = { isTvCinematicMode = false }
        )
        return
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Ambient background layer when active
        if (isAmbientEnabled && isPlaying) {
            AmbientBackgroundLayer(
                theme = selectedAmbientTheme,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Ambient Selection Header Bar
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 6.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = selectedAmbientTheme.iconEmoji,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Arka Plan Ambiyans Videosu",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Okyanus, Şömine, Türkiye Turizmi ve Mevsimler",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Fullscreen TV Mode button
                            IconButton(
                                onClick = { isTvCinematicMode = true },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                    .testTag("tv_cinematic_mode_btn")
                            ) {
                                Icon(
                                    Icons.Default.Tv,
                                    contentDescription = "TV Box Sinematik Modu",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Switch(
                                checked = isAmbientEnabled,
                                onCheckedChange = onToggleAmbientEnabled
                            )
                        }
                    }

                    // Ambient theme pills
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        items(ambientThemes) { theme ->
                            val isSelected = selectedAmbientTheme.id == theme.id
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else Color(0x33000000),
                                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0x44FFFFFF)),
                                modifier = Modifier
                                    .clickable { onSelectAmbientTheme(theme) }
                                    .focusable(true)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(text = theme.iconEmoji, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = theme.title.split(" ").take(2).joinToString(" "),
                                        color = if (isSelected) Color.Black else Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Now Playing Audio Banner
            if (currentStation != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF1E1B4B), Color(0xFF312E81), Color(0xFF4338CA))
                                )
                            )
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x33FFFFFF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Radio,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = currentStation.name,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        if (isPlaying) {
                                            AudioVisualizer(modifier = Modifier.height(14.dp))
                                        }
                                    }
                                    Text(
                                        text = currentStation.currentTrack,
                                        color = Color(0xFFE0E7FF),
                                        fontSize = 12.sp,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "Arka Plan: ${selectedAmbientTheme.iconEmoji} ${selectedAmbientTheme.title}",
                                        color = Color(0xFF93C5FD),
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            IconButton(
                                onClick = onTogglePlay,
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .focusable(true)
                            ) {
                                Icon(
                                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Durdur" else "Başlat",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = "CANLI RADYO İSTASYONLARI (${radios.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 6.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(radios) { station ->
                    val isSelected = currentStation?.id == station.id

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPlayStation(station) }
                            .focusable(true)
                            .testTag("radio_station_${station.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else Color(0xFF1E293B)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.Black else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = station.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = station.frequency,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Text(
                                    text = "${station.genre} • ${station.currentTrack}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }

                            IconButton(
                                onClick = { onPlayStation(station) },
                                modifier = Modifier.focusable(true)
                            ) {
                                Icon(
                                    if (isSelected && isPlaying) Icons.Default.PauseCircleFilled else Icons.Default.PlayCircleFilled,
                                    contentDescription = "Dinle",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// TV Box & Fullscreen Cinematic View
@Composable
private fun TvCinematicAmbientView(
    theme: AmbientTheme,
    station: RadioStation?,
    isPlaying: Boolean,
    themes: List<AmbientTheme>,
    onSelectTheme: (AmbientTheme) -> Unit,
    onTogglePlay: () -> Unit,
    onExitTvMode: () -> Unit
) {
    val currentTime = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Ambient background (ocean, fire, turkey, rain, snow, autumn, spring)
        AmbientBackgroundLayer(theme = theme, modifier = Modifier.fillMaxSize())

        // Top TV Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0x66000000),
                    modifier = Modifier.clickable { onExitTvMode() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Çık", tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "TV Modundan Çık", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // TV Clock & MZD Brand
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "MZD Live TV",
                    color = Color(0xFF38BDF8),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0x66000000)
                ) {
                    Text(
                        text = currentTime,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Bottom HUD
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(20.dp)
        ) {
            // Theme fast selector bar for TV Box Remote
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(bottom = 14.dp)
            ) {
                items(themes) { t ->
                    val isSelected = t.id == theme.id
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0x88000000),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) MaterialTheme.colorScheme.primary else Color(0x55FFFFFF)
                        ),
                        modifier = Modifier
                            .clickable { onSelectTheme(t) }
                            .focusable(true)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(text = t.iconEmoji, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = t.title,
                                color = if (isSelected) Color.Black else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Station bar
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xCC0B0F19),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF38BDF8).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Radio, contentDescription = null, tint = Color(0xFF38BDF8))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = station?.name ?: "Radyo Seçin",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                if (isPlaying) {
                                    AudioVisualizer(modifier = Modifier.height(14.dp))
                                }
                            }
                            Text(
                                text = station?.currentTrack ?: "Canlı Akış",
                                color = Color(0xFFCBD5E1),
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onTogglePlay,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .focusable(true)
                    ) {
                        Icon(
                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Oynat/Durdur",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AudioVisualizer(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_anim")
    val h1 by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 14f,
        animationSpec = infiniteRepeatable(tween(300, easing = LinearEasing), RepeatMode.Reverse),
        label = "h1"
    )
    val h2 by infiniteTransition.animateFloat(
        initialValue = 12f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(tween(250, easing = LinearEasing), RepeatMode.Reverse),
        label = "h2"
    )
    val h3 by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 16f,
        animationSpec = infiniteRepeatable(tween(350, easing = LinearEasing), RepeatMode.Reverse),
        label = "h3"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(modifier = Modifier.width(3.dp).height(h1.dp).background(Color(0xFF38BDF8), CircleShape))
        Box(modifier = Modifier.width(3.dp).height(h2.dp).background(Color(0xFF818CF8), CircleShape))
        Box(modifier = Modifier.width(3.dp).height(h3.dp).background(Color(0xFF38BDF8), CircleShape))
    }
}
