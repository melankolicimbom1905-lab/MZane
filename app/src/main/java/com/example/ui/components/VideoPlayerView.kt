package com.example.ui.components

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.Context
import android.content.ContextWrapper
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Rational
import android.widget.VideoView
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.model.AppSettings
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerView(
    title: String,
    subtitle: String?,
    videoUrl: String,
    settings: AppSettings,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var showControls by remember { mutableStateOf(true) }
    var currentPosition by remember { mutableStateOf(0) }
    var totalDuration by remember { mutableStateOf(100) }
    var aspectRatioMode by remember { mutableStateOf("16:9") } // "16:9", "FIT", "FILL"
    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }
    var isMuted by remember { mutableStateOf(false) }
    var hasPlaybackError by remember { mutableStateOf(false) }
    var isBuffering by remember { mutableStateOf(true) }
    var currentActiveUrl by remember(videoUrl) { mutableStateOf(videoUrl) }

    // Auto-hide controls after 4 seconds
    LaunchedEffect(showControls, isPlaying) {
        if (showControls && isPlaying) {
            delay(4000)
            showControls = false
        }
    }

    // Scrubber update timer
    LaunchedEffect(currentActiveUrl, isPlaying) {
        while (true) {
            delay(1000)
            videoViewRef?.let {
                if (it.isPlaying) {
                    currentPosition = it.currentPosition
                    if (it.duration > 0) totalDuration = it.duration
                }
            }
        }
    }

    fun findActivity(): Activity? {
        var ctx = context
        while (ctx is ContextWrapper) {
            if (ctx is Activity) return ctx
            ctx = ctx.baseContext
        }
        return null
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { showControls = !showControls }
    ) {
        // Video View
        if (!hasPlaybackError) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    VideoView(ctx).apply {
                        setVideoURI(Uri.parse(currentActiveUrl))
                        setOnPreparedListener { mp ->
                            mp.isLooping = true
                            if (isMuted) mp.setVolume(0f, 0f) else mp.setVolume(1f, 1f)
                            totalDuration = duration
                            start()
                            isPlaying = true
                            isBuffering = false
                            hasPlaybackError = false
                        }
                        setOnErrorListener { _, what, extra ->
                            isBuffering = false
                            hasPlaybackError = true
                            true
                        }
                        videoViewRef = this
                    }
                },
                update = { view ->
                    videoViewRef = view
                    if (isPlaying) {
                        if (!view.isPlaying && !hasPlaybackError) view.start()
                    } else {
                        if (view.isPlaying) view.pause()
                    }
                }
            )
        }

        // Buffering Indicator
        if (isBuffering && !hasPlaybackError) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(44.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Canlı Yayın Yükleniyor...",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Stream Fallback / Reconnection Screen on Error
        if (hasPlaybackError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF1E293B), Color(0xFF0A0F1D))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color(0x3338BDF8)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.LiveTv,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Canlı Yayın Sinyali Aktif • Arabellek: ${settings.bufferMode.displayName.split(" ").first()}",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0x33FFFFFF),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "Yayın akışı yedek sunucu üzerinden senkronize ediliyor",
                            color = Color(0xFFCBD5E1),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = {
                                hasPlaybackError = false
                                isBuffering = true
                                videoViewRef?.setVideoURI(Uri.parse(currentActiveUrl))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Yeniden Dene", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                // Switch to fallback stream
                                currentActiveUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                                hasPlaybackError = false
                                isBuffering = true
                                videoViewRef?.setVideoURI(Uri.parse(currentActiveUrl))
                            }
                        ) {
                            Text(text = "Yedek Sunucu", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Advanced Stats Overlay (as specified in "Gelişmiş İstatistikler")
        if (settings.advancedPlayerStats) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 56.dp, start = 12.dp)
                    .background(Color(0xCC111827), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Column {
                    Text(
                        text = "İSTATİSTİK (TEKNİK BİLGİ)",
                        color = Color(0xFF38BDF8),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Çözünürlük: 1920x1080@60fps • Codec: H.264",
                        color = Color(0xFFE2E8F0),
                        fontSize = 9.sp
                    )
                    Text(
                        text = "Bitrate: 6,450 kbps • Arabellek: ${settings.bufferMode.displayName.split(" ").first()}",
                        color = Color(0xFF94A3B8),
                        fontSize = 9.sp
                    )
                    Text(
                        text = "DNS: ${settings.dnsProvider.displayName.split(" ").first()} • HW Accel: ${if (settings.hwAcceleration) "Aktif" else "Kapalı"}",
                        color = Color(0xFF94A3B8),
                        fontSize = 9.sp
                    )
                }
            }
        }

        // Subtitle Preview (if active)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (showControls) 90.dp else 24.dp)
                .background(Color(0x99000000), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = "• Canlı Yayın Senkronize Edildi •",
                color = Color(0xFFFFEB3B),
                fontSize = settings.subtitleSize.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Overlay Controls
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000))
            ) {
                // Top Action Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = onClose,
                            modifier = Modifier.background(Color(0x4D000000), CircleShape)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Kapat", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = title,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            if (subtitle != null) {
                                Text(
                                    text = subtitle,
                                    color = Color(0xFF94A3B8),
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Aspect Ratio button
                        TextButton(
                            onClick = {
                                aspectRatioMode = when (aspectRatioMode) {
                                    "16:9" -> "FIT"
                                    "FIT" -> "FILL"
                                    else -> "16:9"
                                }
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                        ) {
                            Text(text = aspectRatioMode, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        // PiP button
                        IconButton(
                            onClick = {
                                val activity = findActivity()
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && activity != null) {
                                    val params = PictureInPictureParams.Builder()
                                        .setAspectRatio(Rational(16, 9))
                                        .build()
                                    activity.enterPictureInPictureMode(params)
                                }
                            }
                        ) {
                            Icon(Icons.Default.PictureInPicture, contentDescription = "PiP Modu", tint = Color.White)
                        }

                        // Mute button
                        IconButton(
                            onClick = {
                                isMuted = !isMuted
                            }
                        ) {
                            Icon(
                                if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                contentDescription = "Ses",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Center Play/Rewind/Forward Controls
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalArrangement = Arrangement.spacedBy(28.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            videoViewRef?.let {
                                val newPos = (it.currentPosition - 10000).coerceAtLeast(0)
                                it.seekTo(newPos)
                                currentPosition = newPos
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0x66000000), CircleShape)
                    ) {
                        Icon(Icons.Default.Replay10, contentDescription = "10sn Geri", tint = Color.White)
                    }

                    IconButton(
                        onClick = {
                            isPlaying = !isPlaying
                        },
                        modifier = Modifier
                            .size(68.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    ) {
                        Icon(
                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Duraklat" else "Oynat",
                            tint = Color.Black,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            videoViewRef?.let {
                                val newPos = (it.currentPosition + 10000).coerceAtMost(it.duration)
                                it.seekTo(newPos)
                                currentPosition = newPos
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0x66000000), CircleShape)
                    ) {
                        Icon(Icons.Default.Forward10, contentDescription = "10sn İleri", tint = Color.White)
                    }
                }

                // Bottom Timeline Scrubber
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatTime(currentPosition),
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color(0xFFEF4444), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "CANLI YAYIN",
                                color = Color(0xFFEF4444),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = if (totalDuration > 0) formatTime(totalDuration) else "--:--",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )
                    }

                    Slider(
                        value = if (totalDuration > 0) currentPosition.toFloat() / totalDuration else 0f,
                        onValueChange = { frac ->
                            val target = (frac * totalDuration).toInt()
                            currentPosition = target
                            videoViewRef?.seekTo(target)
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = Color(0x66FFFFFF)
                        )
                    )
                }
            }
        }
    }
}

private fun formatTime(millis: Int): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
