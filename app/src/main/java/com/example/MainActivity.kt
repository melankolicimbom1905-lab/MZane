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
import com.example.ui.HomeScreen
import com.example.ui.theme.MZaneTheme
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MZaneTheme {
                MainScreen()
            }
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

        ToolScreen.LOGIN -> {
            LoginScreen(
                onLogin = { email, password ->
                    viewModel.login(email, password)
                },
                onBack = {
                    viewModel.closeTool()
                }
            )
        }

        ToolScreen.HOME -> {
            HomeScreen(
                onOpenAccount = {
                    viewModel.openTool(ToolScreen.ACCOUNT)
                },
                onOpenLogin = {
                    viewModel.openTool(ToolScreen.LOGIN)
                }
            )
        }
    }
}
