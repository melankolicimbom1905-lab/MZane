package com.example.ui.components

import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.example.model.AmbientTheme
import kotlin.random.Random

@Composable
fun AmbientBackgroundLayer(
    theme: AmbientTheme,
    modifier: Modifier = Modifier
) {
    var videoError by remember(theme.id) { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        // Base ambient video player (plays muted on loop)
        if (!videoError) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    VideoView(ctx).apply {
                        setVideoURI(Uri.parse(theme.videoUrl))
                        setOnPreparedListener { mp ->
                            mp.isLooping = true
                            mp.setVolume(0f, 0f)
                            start()
                        }
                        setOnErrorListener { _, _, _ ->
                            videoError = true
                            true
                        }
                    }
                },
                update = { view ->
                    if (!videoError && !view.isPlaying) {
                        try {
                            view.start()
                        } catch (e: Exception) {}
                    }
                }
            )
        }

        // Animated Procedural Canvas Atmosphere Overlay
        when (theme.effectType) {
            "OCEAN" -> OceanAmbientCanvas()
            "FIRE" -> FireAmbientCanvas()
            "TURKEY" -> TurkeyTourismCanvas()
            "RAIN" -> RainAmbientCanvas()
            "SNOW" -> SnowAmbientCanvas()
            "AUTUMN" -> AutumnAmbientCanvas()
            "SPRING" -> SpringAmbientCanvas()
            else -> OceanAmbientCanvas()
        }

        // Subtle dark vignette gradient for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x990A0E17),
                            Color(0x400A0E17),
                            Color(0xCC0A0E17)
                        )
                    )
                )
        )
    }
}

// 1. Ocean Bubbles & Rays
@Composable
private fun OceanAmbientCanvas() {
    val transition = rememberInfiniteTransition(label = "ocean_anim")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Restart),
        label = "phase"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Background water gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF031B33), Color(0xFF023E5E), Color(0xFF001524))
            )
        )

        // Rising water bubbles
        for (i in 0..18) {
            val randomX = ((i * 53) % width.toInt()).toFloat()
            val speedFactor = (i % 5) + 1
            val currentY = (height - ((phase * speedFactor * height + i * 40) % height))
            val radius = (4 + (i % 6)).toFloat()

            drawCircle(
                color = Color(0x6638BDF8),
                radius = radius,
                center = Offset(randomX, currentY)
            )
        }
    }
}

// 2. Cozy Fireplace Particles
@Composable
private fun FireAmbientCanvas() {
    val transition = rememberInfiniteTransition(label = "fire_anim")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), RepeatMode.Restart),
        label = "fire"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Warm fiery hearth glow
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFB45309), Color(0xFF78350F), Color(0xFF1C0E07)),
                center = Offset(width / 2f, height * 0.8f),
                radius = width * 0.9f
            )
        )

        // Floating glowing embers
        for (i in 0..24) {
            val startX = (width * 0.2f + (i * 29) % (width * 0.6f))
            val currentY = height * 0.9f - ((phase * height * 0.7f + i * 35) % (height * 0.7f))
            val drift = kotlin.math.sin(phase * 6.28f + i) * 20f

            drawCircle(
                color = if (i % 2 == 0) Color(0xCCF59E0B) else Color(0xCCEF4444),
                radius = (3 + (i % 4)).toFloat(),
                center = Offset(startX + drift, currentY)
            )
        }
    }
}

// 3. Turkey Tourism (Cappadocia Balonları & Boğaz)
@Composable
private fun TurkeyTourismCanvas() {
    val transition = rememberInfiniteTransition(label = "turkey_anim")
    val floatAnim by transition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(tween(3000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "balon"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Sunrise Cappadocia sky
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF1E1B4B), Color(0xFF831843), Color(0xFFD97706), Color(0xFF1E293B))
            )
        )

        // Hot Air Balloons silhouettes
        val balloonPositions = listOf(
            Offset(width * 0.25f, height * 0.35f + floatAnim),
            Offset(width * 0.65f, height * 0.25f - floatAnim * 0.8f),
            Offset(width * 0.82f, height * 0.45f + floatAnim * 0.6f)
        )

        balloonPositions.forEachIndexed { idx, pos ->
            val scale = if (idx == 0) 22f else 16f
            // Balloon top
            drawCircle(
                color = Color(0xDDFFFFFF),
                radius = scale,
                center = pos
            )
            // Basket
            drawRect(
                color = Color(0xDDFFFFFF),
                topLeft = Offset(pos.x - scale * 0.3f, pos.y + scale * 1.2f),
                size = androidx.compose.ui.geometry.Size(scale * 0.6f, scale * 0.4f)
            )
        }
    }
}

// 4. Soothing Rain & Water Droplets
@Composable
private fun RainAmbientCanvas() {
    val transition = rememberInfiniteTransition(label = "rain_anim")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(800, easing = LinearEasing), RepeatMode.Restart),
        label = "rain"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Stormy overcast gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF0B0F19))
            )
        )

        // Diagonal falling rain streaks
        for (i in 0..40) {
            val startX = ((i * 37) % width.toInt()).toFloat()
            val startY = (phase * height + (i * 47)) % height
            val length = 35f

            drawLine(
                color = Color(0x6638BDF8),
                start = Offset(startX, startY),
                end = Offset(startX - 10f, startY + length),
                strokeWidth = 2.5f
            )
        }
    }
}

// 5. Winter Snowfall
@Composable
private fun SnowAmbientCanvas() {
    val transition = rememberInfiniteTransition(label = "snow_anim")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Restart),
        label = "snow"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Winter twilight gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF0B192C), Color(0xFF1E3E62), Color(0xFF000000))
            )
        )

        // Falling snowflakes
        for (i in 0..35) {
            val randomX = ((i * 43) % width.toInt()).toFloat()
            val drift = kotlin.math.sin(phase * 6.28f + i) * 25f
            val startY = (phase * height * ((i % 4) + 1) * 0.5f + i * 30) % height
            val flakeRadius = (2.5f + (i % 4)).toFloat()

            drawCircle(
                color = Color(0xCCFFFFFF),
                radius = flakeRadius,
                center = Offset(randomX + drift, startY)
            )
        }
    }
}

// 6. Autumn Falling Leaves
@Composable
private fun AutumnAmbientCanvas() {
    val transition = rememberInfiniteTransition(label = "autumn_anim")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4500, easing = LinearEasing), RepeatMode.Restart),
        label = "autumn"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Warm golden autumn forest
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF451A03), Color(0xFF78350F), Color(0xFF1A0A02))
            )
        )

        // Swaying golden leaves
        for (i in 0..22) {
            val startX = ((i * 47) % width.toInt()).toFloat()
            val drift = kotlin.math.cos(phase * 5f + i) * 35f
            val startY = (phase * height + i * 45) % height
            val leafColor = if (i % 2 == 0) Color(0xDDF59E0B) else Color(0xDDDC2626)

            drawCircle(
                color = leafColor,
                radius = (5 + (i % 4)).toFloat(),
                center = Offset(startX + drift, startY)
            )
        }
    }
}

// 7. Spring Blooming Nature
@Composable
private fun SpringAmbientCanvas() {
    val transition = rememberInfiniteTransition(label = "spring_anim")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3500, easing = LinearEasing), RepeatMode.Restart),
        label = "spring"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Fresh spring meadow
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF064E3B), Color(0xFF047857), Color(0xFF022C22))
            )
        )

        // Floating pink blossom petals
        for (i in 0..20) {
            val startX = ((i * 41) % width.toInt()).toFloat()
            val drift = kotlin.math.sin(phase * 4f + i) * 30f
            val startY = (phase * height + i * 40) % height

            drawCircle(
                color = Color(0xCCF472B6),
                radius = (4 + (i % 3)).toFloat(),
                center = Offset(startX + drift, startY)
            )
        }
    }
}
