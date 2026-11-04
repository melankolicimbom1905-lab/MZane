package com.example.util

import com.example.model.Channel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

object M3uParser {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .build()

    suspend fun fetchFromUrl(url: String): List<Channel> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "MZD/5.4.18 (Android TV; IPTV)")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext emptyList()
                }
                val body = response.body?.string() ?: return@withContext emptyList()
                parse(body)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun parse(content: String): List<Channel> {
        val channels = mutableListOf<Channel>()
        val lines = content.lines()

        var currentGroup = "Genel TV"
        var currentName = ""
        var currentLogo = ""
        var channelIndex = 1

        val groupTitleRegex = Pattern.compile("""group-title="([^"]*)"""", Pattern.CASE_INSENSITIVE)
        val tvgNameRegex = Pattern.compile("""tvg-name="([^"]*)"""", Pattern.CASE_INSENSITIVE)
        val tvgLogoRegex = Pattern.compile("""tvg-logo="([^"]*)"""", Pattern.CASE_INSENSITIVE)

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) continue

            if (trimmed.startsWith("#EXTINF:", ignoreCase = true)) {
                // Parse group-title
                val groupMatcher = groupTitleRegex.matcher(trimmed)
                currentGroup = if (groupMatcher.find()) {
                    groupMatcher.group(1)?.trim()?.ifEmpty { "Genel TV" } ?: "Genel TV"
                } else {
                    "Genel TV"
                }

                // Parse tvg-name
                val nameMatcher = tvgNameRegex.matcher(trimmed)
                val tvgName = if (nameMatcher.find()) nameMatcher.group(1)?.trim() else null

                // Parse tvg-logo
                val logoMatcher = tvgLogoRegex.matcher(trimmed)
                currentLogo = if (logoMatcher.find()) logoMatcher.group(1)?.trim() ?: "" else ""

                // Channel name is after comma
                val commaIndex = trimmed.lastIndexOf(',')
                currentName = if (commaIndex != -1 && commaIndex < trimmed.length - 1) {
                    trimmed.substring(commaIndex + 1).trim()
                } else {
                    tvgName ?: "Kanal $channelIndex"
                }
            } else if (!trimmed.startsWith("#") && (trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("rtmp://"))) {
                val streamUrl = trimmed
                val logoText = if (currentName.length >= 3) {
                    currentName.take(3).uppercase()
                } else {
                    "CH$channelIndex"
                }

                val resolution = when {
                    currentName.contains("4K", ignoreCase = true) || streamUrl.contains("4k", ignoreCase = true) -> "4K UHD"
                    currentName.contains("FHD", ignoreCase = true) || currentName.contains("1080", ignoreCase = true) -> "FHD"
                    currentName.contains("HD", ignoreCase = true) || currentName.contains("720", ignoreCase = true) -> "HD"
                    else -> "FHD"
                }

                val isAdult = currentGroup.contains("Adult", ignoreCase = true) ||
                        currentGroup.contains("+18", ignoreCase = true) ||
                        currentName.contains("+18", ignoreCase = true) ||
                        currentGroup.contains("Yetişkin", ignoreCase = true)

                channels.add(
                    Channel(
                        id = "m3u_${System.currentTimeMillis()}_$channelIndex",
                        channelNumber = channelIndex,
                        name = currentName,
                        category = currentGroup,
                        streamUrl = streamUrl,
                        logoText = logoText,
                        currentEpg = "Canlı Yayın ($currentGroup)",
                        nextEpg = "Sonraki Program",
                        isFavorite = false,
                        resolution = resolution,
                        isAdult = isAdult
                    )
                )

                channelIndex++
                // Reset for next entry
                currentGroup = "Genel TV"
                currentName = ""
                currentLogo = ""
            }
        }

        return channels
    }

    fun getDemoM3uList(): List<Channel> {
        val sampleM3uString = """
#EXTM3U
#EXTINF:-1 tvg-id="bein1" tvg-name="beIN SPORTS 1 HD" group-title="Spor",beIN SPORTS 1 HD
https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4
#EXTINF:-1 tvg-id="ssport1" tvg-name="S Sport 1 FHD" group-title="Spor",S Sport 1 FHD
https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4
#EXTINF:-1 tvg-id="trtspor" tvg-name="TRT Spor Yıldız HD" group-title="Spor",TRT Spor Yıldız HD
https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4
#EXTINF:-1 tvg-id="sinematv" tvg-name="Sinema TV Aksiyon 4K" group-title="Sinema",Sinema TV Aksiyon 4K
https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4
#EXTINF:-1 tvg-id="premiere" tvg-name="beIN Movies Premiere HD" group-title="Sinema",beIN Movies Premiere HD
https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4
#EXTINF:-1 tvg-id="natgeo" tvg-name="National Geographic Wild HD" group-title="Belgesel",National Geographic Wild HD
https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4
#EXTINF:-1 tvg-id="discovery" tvg-name="Discovery Channel FHD" group-title="Belgesel",Discovery Channel FHD
https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4
#EXTINF:-1 tvg-id="kanald" tvg-name="Kanal D HD" group-title="Ulusal",Kanal D HD
https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4
#EXTINF:-1 tvg-id="cn" tvg-name="Cartoon Network Türkiye" group-title="Çocuk",Cartoon Network Türkiye
https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4
        """.trimIndent()
        return parse(sampleM3uString)
    }
}
