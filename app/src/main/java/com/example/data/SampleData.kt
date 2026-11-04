package com.example.data

import com.example.model.*

object SampleData {
    val sampleProfiles = listOf(
        UserProfile("p1", "Profil 1 (Ana)", 0xFF0284C7, isKids = false),
        UserProfile("p2", "Profil 2 (Salon TV)", 0xFF8B5CF6, isKids = false),
        UserProfile("p3", "Çocuk Profili", 0xFF10B981, isKids = true)
    )

    val sampleChannels = listOf(
        Channel(
            id = "c1",
            channelNumber = 1,
            name = "beIN SPORTS 1 HD",
            category = "Spor",
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            logoText = "SP1",
            currentEpg = "Galatasaray - Beşiktaş Derbisi",
            nextEpg = "Maç Sonu Stüdyo & Özetler",
            isFavorite = true,
            resolution = "4K HDR"
        ),
        Channel(
            id = "c2",
            channelNumber = 2,
            name = "S Sport 1 HD",
            category = "Spor",
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            logoText = "SS1",
            currentEpg = "Premier Lig: Manchester City - Arsenal",
            nextEpg = "Serie A: Inter - Juventus",
            isFavorite = true,
            resolution = "FHD 60FPS"
        ),
        Channel(
            id = "c3",
            channelNumber = 3,
            name = "TRT Spor Yıldız",
            category = "Spor",
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            logoText = "TRT",
            currentEpg = "AFC Şampiyonlar Ligi: Al Hilal - Al Nassr",
            nextEpg = "Spor Manşet",
            isFavorite = false,
            resolution = "FHD"
        ),
        Channel(
            id = "c4",
            channelNumber = 4,
            name = "Sinema TV Aksiyon HD",
            category = "Sinema",
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            logoText = "S-AK",
            currentEpg = "John Wick: Chapter 4",
            nextEpg = "Mad Max: Fury Road",
            isFavorite = true,
            resolution = "4K UHD"
        ),
        Channel(
            id = "c5",
            channelNumber = 5,
            name = "beIN Movies Premiere",
            category = "Sinema",
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            logoText = "MOV",
            currentEpg = "Oppenheimer (2023)",
            nextEpg = "Dune: Part Two",
            isFavorite = false,
            resolution = "FHD"
        ),
        Channel(
            id = "c6",
            channelNumber = 6,
            name = "National Geographic HD",
            category = "Belgesel",
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            logoText = "NAT",
            currentEpg = "Vahşi Krallık: Afrika Aslanları",
            nextEpg = "Büyük Kedilerin Dünyası",
            isFavorite = true,
            resolution = "4K"
        ),
        Channel(
            id = "c7",
            channelNumber = 7,
            name = "Discovery Channel HD",
            category = "Belgesel",
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            logoText = "DISC",
            currentEpg = "Deadliest Catch: Fırtınada Av",
            nextEpg = "Nasıl Yapılır?",
            isFavorite = false,
            resolution = "FHD"
        ),
        Channel(
            id = "c8",
            channelNumber = 8,
            name = "Kanal D HD",
            category = "Ulusal",
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
            logoText = "KND",
            currentEpg = "İnci Taneleri - Canlı Yayın",
            nextEpg = "Ana Haber Bülteni",
            isFavorite = false,
            resolution = "FHD"
        ),
        Channel(
            id = "c9",
            channelNumber = 9,
            name = "Cartoon Network HD",
            category = "Çocuk",
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
            logoText = "CN",
            currentEpg = "Gumball'ın Muhteşem Dünyası",
            nextEpg = "Sürekli Dizi",
            isFavorite = true,
            resolution = "HD"
        ),
        Channel(
            id = "c10",
            channelNumber = 10,
            name = "Adult Zone VIP 18+",
            category = "Yetişkin (+18)",
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            logoText = "18+",
            currentEpg = "Gece Kuşağı Özel Yayın",
            nextEpg = "VIP Sinema",
            isFavorite = false,
            resolution = "FHD",
            isAdult = true
        )
    )

    val sampleMovies = listOf(
        Movie(
            id = "m1",
            title = "Gladyatör II (2024)",
            category = "Aksiyon & Macera",
            year = 2024,
            duration = "2s 28dk",
            rating = 8.4,
            plot = "Yıllar sonra Colosseum'a dönen Lucius, Roma'nın kaderini yeniden çizmek için arenada savaşıyor.",
            genre = "Aksiyon / Tarih / Dram",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            isNew = true,
            isFavorite = true
        ),
        Movie(
            id = "m2",
            title = "Dune: Part Two",
            category = "Bilim Kurgu",
            year = 2024,
            duration = "2s 46dk",
            rating = 8.7,
            plot = "Paul Atreides, ailesini yok eden komploculara karşı Fremenler ile ittifak kurarak intikam savaşı başlatır.",
            genre = "Bilim Kurgu / Macera",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            isNew = true,
            isFavorite = true
        ),
        Movie(
            id = "m3",
            title = "Deadpool & Wolverine",
            category = "Komedi & Aksiyon",
            year = 2024,
            duration = "2s 08dk",
            rating = 8.1,
            plot = "Zaman Varyansı Otoritesi tarafından göreve çağrılan Deadpool, Wolverine ile ortak olmak zorundadır.",
            genre = "Komedi / Marvel / Macera",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            isNew = true,
            isFavorite = false
        ),
        Movie(
            id = "m4",
            title = "Oppenheimer",
            category = "Biyografi & Dram",
            year = 2023,
            duration = "3s 00dk",
            rating = 8.9,
            plot = "Manhattan Projesi lideri J. Robert Oppenheimer'ın atom bombasını geliştirme süreci ve sonrasındaki vicdani hesaplaşması.",
            genre = "Biyografi / Tarih",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            isNew = false,
            isFavorite = true
        ),
        Movie(
            id = "m5",
            title = "Ters Yüz 2 (Inside Out 2)",
            category = "Animasyon",
            year = 2024,
            duration = "1s 36dk",
            rating = 8.0,
            plot = "Riley ergenlik çağına girerken kumanda merkezine yeni duygular gelir: Kaygı, Gıpta, Bıkkınlık ve Utanç.",
            genre = "Animasyon / Aile",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            isNew = true,
            isFavorite = false
        )
    )

    val sampleSeries = listOf(
        Series(
            id = "s1",
            title = "House of the Dragon",
            category = "Fantastik & Dram",
            rating = 8.5,
            totalSeasons = 2,
            plot = "Targaryen hanedanının çöküşüne ve Ejderhaların Dansı olarak bilinen iç savaşa odaklanan epik hikaye.",
            isNew = true,
            isFavorite = true,
            episodes = listOf(
                Episode(2, 1, "Göze Göz, Dişe Diş", "64 dk", "16 Haz 2024", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"),
                Episode(2, 2, "Rhaenyra Kızıl", "69 dk", "23 Haz 2024", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"),
                Episode(2, 3, "Yel Değirmeni Değirmeni", "66 dk", "30 Haz 2024", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
                Episode(2, 4, "Kızıl Ejder ve Altın Ejder", "54 dk", "07 Tem 2024", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4")
            )
        ),
        Series(
            id = "s2",
            title = "The Penguin",
            category = "Suç & Gerilim",
            rating = 8.8,
            totalSeasons = 1,
            plot = "Carmine Falcone'nin ölümünün ardından Gotham'ın yeraltı dünyasında güç boşluğunu doldurmaya çalışan Oswald Cobblepot.",
            isNew = true,
            isFavorite = true,
            episodes = listOf(
                Episode(1, 1, "İkinci Şans", "67 dk", "19 Eyl 2024", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
                Episode(1, 2, "İçerideki Köstebek", "56 dk", "29 Eyl 2024", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"),
                Episode(1, 3, "Bliss", "59 dk", "06 Eki 2024", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4")
            )
        ),
        Series(
            id = "s3",
            title = "Stranger Things",
            category = "Bilim Kurgu",
            rating = 8.7,
            totalSeasons = 4,
            plot = "Küçük bir kasabada kaybolan çocuğu ararken gizli deneyler, doğaüstü güçler ve gizemli bir kız ortaya çıkar.",
            isNew = false,
            isFavorite = false,
            episodes = listOf(
                Episode(4, 1, "Bölüm Bir: Cehennem Kulübü", "76 dk", "27 May 2022", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4"),
                Episode(4, 2, "Bölüm İki: Vecna'nın Laneti", "77 dk", "27 May 2022", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4")
            )
        )
    )

    val sampleRadios = listOf(
        RadioStation("r1", "Power FM", "Pop / Yabancı", "100.0 FM", "https://listen.powerapp.com.tr/powerfm/mpeg/icecast.audio", "Dua Lipa - Training Season", isFavorite = true),
        RadioStation("r2", "Power Türk", "Türkçe Pop", "99.8 FM", "https://listen.powerapp.com.tr/powerturk/mpeg/icecast.audio", "Tarkan - Sen Çok Güzelsin", isFavorite = true),
        RadioStation("r3", "Virgin Radio Türkiye", "Hit / Dans", "101.4 FM", "https://playerservices.streamtheworld.com/api/livestream-redirect/VIRGIN_RADIO.mp3", "Calvin Harris - Miracle", isFavorite = false),
        RadioStation("r4", "Fenomen Lounge", "Lounge / Chill", "Online", "https://ice1.somafm.com/groovesalad-128-mp3", "Sunset Chill Groove Project", isFavorite = true),
        RadioStation("r5", "Süper FM", "Hareketli Türkçe", "90.8 FM", "https://listen.powerapp.com.tr/powerfm/mpeg/icecast.audio", "Edis - Martılar", isFavorite = false),
        RadioStation("r6", "Joy FM", "Slow & Soul", "100.6 FM", "https://listen.powerapp.com.tr/powerfm/mpeg/icecast.audio", "Adele - Easy On Me", isFavorite = false)
    )

    val sampleLiveMatches = listOf(
        LiveMatch(
            id = "lm1",
            team1 = "Al Hilal",
            team2 = "Al Nassr",
            league = "AFC Champions League",
            matchTime = "Canlı Yayın",
            score = "2 - 1",
            isLive = true,
            minute = "67'",
            channelName = "TRT Spor Yıldız",
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
        ),
        LiveMatch(
            id = "lm2",
            team1 = "Galatasaray",
            team2 = "Beşiktaş",
            league = "Trendyol Süper Lig",
            matchTime = "Bugün 20:00",
            score = "1 - 0",
            isLive = true,
            minute = "34'",
            channelName = "beIN SPORTS 1 HD",
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        ),
        LiveMatch(
            id = "lm3",
            team1 = "Real Madrid",
            team2 = "Manchester City",
            league = "UEFA Şampiyonlar Ligi",
            matchTime = "Bugün 22:00",
            score = "0 - 0",
            isLive = false,
            minute = "Yakında",
            channelName = "S Sport 1 HD",
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
        )
    )

    val sampleSeriesCalendar = listOf(
        SeriesCalendarItem("sc1", "House of the Dragon", "S02E05 - Ejderhaların Kanı", "Bugün", "Çarşamba", "22:00", "beIN Connect"),
        SeriesCalendarItem("sc2", "The Penguin", "S01E04 - Bliss ve Karanlık", "Yarın", "Perşembe", "21:30", "HBO Max"),
        SeriesCalendarItem("sc3", "Yüzüklerin Efendisi: Güç Yüzükleri", "S02E06 - Eregion Kuşatması", "Cuma", "Cuma", "20:00", "Prime Video"),
        SeriesCalendarItem("sc4", "Severance", "S02E01 - Ayrılışın İçi", "Pazar", "Pazar", "21:00", "Apple TV+")
    )

    val sampleSportsRadar = listOf(
        SportsRadarItem("sr1", "Al Hilal", "Al Nassr", "AFC Champions League", "67'", 2, 1, "Canlı", "⚽ Mitrovic gol (62')"),
        SportsRadarItem("sr2", "Galatasaray", "Beşiktaş", "Trendyol Süper Lig", "34'", 1, 0, "Canlı", "⚽ Icardi gol (21')"),
        SportsRadarItem("sr3", "Arsenal", "Chelsea", "Premier Lig", "89'", 3, 1, "Canlı", "🟨 Sarı kart (84')"),
        SportsRadarItem("sr4", "Bayern Münih", "Dortmund", "Bundesliga", "MS", 4, 2, "Bitti", "Maç sona erdi"),
        SportsRadarItem("sr5", "Barcelona", "Atletico Madrid", "La Liga", "22:00", 0, 0, "Başlamadı", "Kadrolar açıklandı")
    )

    val sampleAmbientThemes = listOf(
        AmbientTheme(
            id = "ocean",
            title = "Okyanus Altı & Mercan Resifleri",
            category = "Doğa & Deniz",
            iconEmoji = "🌊",
            description = "Tropik balıklar, mercanlar ve huzur veren derin deniz ışıkları",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            effectType = "OCEAN"
        ),
        AmbientTheme(
            id = "fire",
            title = "Çıtırdayan Şömine",
            category = "Sıcak Ambiyans",
            iconEmoji = "🔥",
            description = "Alevlerin dansı ve dinlendirici sıcak şömine odunları",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            effectType = "FIRE"
        ),
        AmbientTheme(
            id = "turkey",
            title = "Türkiye Turizm & İlkleri",
            category = "Turizm & Keşif",
            iconEmoji = "🇹🇷",
            description = "Kapadokya peri bacaları, Pamukkale travertenleri ve Boğaz manzarası",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            effectType = "TURKEY"
        ),
        AmbientTheme(
            id = "rain",
            title = "Huzurlu Yağmur",
            category = "Mevsimler",
            iconEmoji = "🌧️",
            description = "Cama vuran yağmur damlaları ve sisli orman dinginliği",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            effectType = "RAIN"
        ),
        AmbientTheme(
            id = "snow",
            title = "Kış & Lapa Lapa Kar",
            category = "Mevsimler",
            iconEmoji = "❄️",
            description = "Beyaza bürünmüş karlı dağlar ve süzülen kar taneleri",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            effectType = "SNOW"
        ),
        AmbientTheme(
            id = "autumn",
            title = "Sonbahar Ormanı",
            category = "Mevsimler",
            iconEmoji = "🍂",
            description = "Altın sarısı dökülen yapraklar ve doğanın sonbahar renkleri",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            effectType = "AUTUMN"
        ),
        AmbientTheme(
            id = "spring",
            title = "İlkbahar & Çiçek Açan Doğa",
            category = "Mevsimler",
            iconEmoji = "🌸",
            description = "Akan berrak ırmaklar ve bahar çiçeklerinin canlanışı",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            effectType = "SPRING"
        )
    )

    val defaultPlaylists = listOf(
        M3uPlaylist(
            id = "pl_default",
            name = "MZD VIP Ulusal & Spor (M3U8)",
            url = "https://stream.globalcdn.live:8080/get.php?username=vip&type=m3u_plus",
            channelCount = 10,
            dateAdded = "Bugün",
            isActive = true,
            channels = sampleChannels
        )
    )
}
