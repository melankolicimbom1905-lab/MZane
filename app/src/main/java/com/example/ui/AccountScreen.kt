package com.example.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.AccountInfo
import com.example.model.Playlist

@Composable
fun AccountScreen(
    accountInfo: AccountInfo,
    playlists: List<Playlist>,
    isLoadingM3u: Boolean,
    onAddPlaylist: (String, String) -> Unit,
    onActivateAndWatch: (Playlist) -> Unit,
    onDeletePlaylist: (String) -> Unit,
    onBack: () -> Unit,
    onLogin: () -> Unit = {}
) {
    var playlistName by remember { mutableStateOf("") }
    var playlistUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Hesap")

        Text(text = "Kullanıcı: ${accountInfo.username.ifBlank { "Misafir" }}")
        Text(text = "Üyelik: ${accountInfo.membershipType}")

        TextButton(onClick = onLogin) {
            Text(text = "Giriş yap")
        }

        OutlinedTextField(
            value = playlistName,
            onValueChange = { playlistName = it },
            label = { Text("Playlist adı") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = playlistUrl,
            onValueChange = { playlistUrl = it },
            label = { Text("M3U URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                onAddPlaylist(playlistName, playlistUrl)
                playlistName = ""
                playlistUrl = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Playlist ekle")
        }

        if (isLoadingM3u) {
            CircularProgressIndicator()
        }

        playlists.forEach { playlist ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = playlist.name)

                Row {
                    TextButton(onClick = { onActivateAndWatch(playlist) }) {
                        Text(text = "İzle")
                    }

                    TextButton(onClick = { onDeletePlaylist(playlist.id) }) {
                        Text(text = "Sil")
                    }
                }
            }
        }

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Geri")
        }
    }
}
