package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AccountInfo
import com.example.ui.AccountScreen
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.ToolScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.currentTool) {
        ToolScreen.ACCOUNT -> {
            AccountScreen(
                accountInfo = AccountInfo(),
                playlists = uiState.playlists,
                isLoadingM3u = uiState.isLoadingM3u,
                onAddPlaylist = { name, url ->
                    viewModel.importM3uPlaylist(name, url)
                },
                onActivateAndWatch = { playlist ->
                    viewModel.activatePlaylistAndWatch(playlist)
                },
                onDeletePlaylist = { id ->
                    viewModel.deletePlaylist(id)
                },
                onBack = {
                    viewModel.closeTool()
                },
                onLogin = {
                    viewModel.openLogin()
                }
            )
        }

        else -> {
            // Burada projenin mevcut ana ekranı çağrılmalı.
            // Eski MainActivity'de hangi ekran vardıysa onu kullan.
        }
    }
}
