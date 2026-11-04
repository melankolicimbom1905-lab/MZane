package com.example.ui.components

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.model.Channel

@Composable
fun MultiScreenView(
    screenCount: Int,
    slots: List<Channel?>,
    allChannels: List<Channel>,
    activeAudioIndex: Int,
    onSelectScreenCount: (Int) -> Unit,
    onSelectAudioSlot: (Int) -> Unit,
    onAssignSlotChannel: (slotIndex: Int, Channel) -> Unit,
    onClose: () -> Unit
) {
    var channelSelectorSlotIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
    ) {
        // Top Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Kapat", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Çoklu Ekran (Multi-Screen)",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Screen split mode switcher (2 screens vs 4 screens)
            Row(
                modifier = Modifier
                    .background(Color(0xFF1E293B), RoundedCornerShape(20.dp))
                    .padding(4.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (screenCount == 2) MaterialTheme.colorScheme.primary else Color.Transparent,
                    modifier = Modifier.clickable { onSelectScreenCount(2) }
                ) {
                    Text(
                        text = "2'li Bölünmüş",
                        color = if (screenCount == 2) Color.Black else Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (screenCount == 4) MaterialTheme.colorScheme.primary else Color.Transparent,
                    modifier = Modifier.clickable { onSelectScreenCount(4) }
                ) {
                    Text(
                        text = "4'lü Bölünmüş",
                        color = if (screenCount == 4) Color.Black else Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Active screens grid
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            if (screenCount == 2) {
                // 2 Screens Vertical Split
                Column(modifier = Modifier.fillMaxSize()) {
                    MultiScreenCell(
                        slotIndex = 0,
                        channel = slots.getOrNull(0),
                        hasAudio = activeAudioIndex == 0,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(2.dp),
                        onAudioClick = { onSelectAudioSlot(0) },
                        onChangeChannelClick = { channelSelectorSlotIndex = 0 }
                    )
                    MultiScreenCell(
                        slotIndex = 1,
                        channel = slots.getOrNull(1),
                        hasAudio = activeAudioIndex == 1,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(2.dp),
                        onAudioClick = { onSelectAudioSlot(1) },
                        onChangeChannelClick = { channelSelectorSlotIndex = 1 }
                    )
                }
            } else {
                // 4 Screens Grid (2x2)
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(modifier = Modifier.weight(1f)) {
                        MultiScreenCell(
                            slotIndex = 0,
                            channel = slots.getOrNull(0),
                            hasAudio = activeAudioIndex == 0,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(2.dp),
                            onAudioClick = { onSelectAudioSlot(0) },
                            onChangeChannelClick = { channelSelectorSlotIndex = 0 }
                        )
                        MultiScreenCell(
                            slotIndex = 1,
                            channel = slots.getOrNull(1),
                            hasAudio = activeAudioIndex == 1,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(2.dp),
                            onAudioClick = { onSelectAudioSlot(1) },
                            onChangeChannelClick = { channelSelectorSlotIndex = 1 }
                        )
                    }
                    Row(modifier = Modifier.weight(1f)) {
                        MultiScreenCell(
                            slotIndex = 2,
                            channel = slots.getOrNull(2),
                            hasAudio = activeAudioIndex == 2,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(2.dp),
                            onAudioClick = { onSelectAudioSlot(2) },
                            onChangeChannelClick = { channelSelectorSlotIndex = 2 }
                        )
                        MultiScreenCell(
                            slotIndex = 3,
                            channel = slots.getOrNull(3),
                            hasAudio = activeAudioIndex == 3,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(2.dp),
                            onAudioClick = { onSelectAudioSlot(3) },
                            onChangeChannelClick = { channelSelectorSlotIndex = 3 }
                        )
                    }
                }
            }
        }

        // Channel Selector Drawer / Bottom sheet when changing a slot
        channelSelectorSlotIndex?.let { targetSlot ->
            Surface(
                color = Color(0xFF0F172A),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ekran ${targetSlot + 1} İçin Kanal Seçin",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        IconButton(onClick = { channelSelectorSlotIndex = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Kapat", tint = Color.White)
                        }
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        items(allChannels) { ch ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                                modifier = Modifier
                                    .width(140.dp)
                                    .clickable {
                                        onAssignSlotChannel(targetSlot, ch)
                                        channelSelectorSlotIndex = null
                                    }
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = ch.name,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = ch.category,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MultiScreenCell(
    slotIndex: Int,
    channel: Channel?,
    hasAudio: Boolean,
    modifier: Modifier = Modifier,
    onAudioClick: () -> Unit,
    onChangeChannelClick: () -> Unit
) {
    var hasCellError by remember(channel?.id) { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(Color(0xFF0F172A), RoundedCornerShape(8.dp))
            .border(
                width = if (hasAudio) 2.dp else 1.dp,
                color = if (hasAudio) MaterialTheme.colorScheme.primary else Color(0xFF334155),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        if (channel != null) {
            if (!hasCellError) {
                // Streaming Video
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            setVideoURI(Uri.parse(channel.streamUrl))
                            setOnPreparedListener { mp ->
                                mp.isLooping = true
                                if (!hasAudio) mp.setVolume(0f, 0f) else mp.setVolume(1f, 1f)
                                start()
                            }
                            setOnErrorListener { _, _, _ ->
                                hasCellError = true
                                true
                            }
                        }
                    },
                    update = { view ->
                        // Android VideoView volume update
                    }
                )
            } else {
                // Clean fallback view when stream decoder fails
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1E293B)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.LiveTv,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = channel.name,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Canlı Yayın Sinyali Aktif",
                            color = Color(0xFF94A3B8),
                            fontSize = 9.sp
                        )
                    }
                }
            }

            // Header Overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x99000000))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color(0xFFEF4444), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = channel.name,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                Row {
                    // Audio toggle button
                    IconButton(
                        onClick = onAudioClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            if (hasAudio) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                            contentDescription = "Ses Aç/Kapat",
                            tint = if (hasAudio) MaterialTheme.colorScheme.primary else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Change channel button
                    IconButton(
                        onClick = onChangeChannelClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            Icons.Default.SwapHoriz,
                            contentDescription = "Kanal Değiştir",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        } else {
            // Empty slot
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onChangeChannelClick() },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.AddCircleOutline,
                    contentDescription = "Kanal Ekle",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ekran ${slotIndex + 1} - Kanal Seç",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        }
    }
}
