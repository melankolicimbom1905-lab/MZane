package com.example

import com.example.model.BottomNavTab
import com.example.model.ThemeType
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.ToolScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun test_main_view_model_flows() {
    val viewModel = MainViewModel()
    assertEquals(BottomNavTab.HOME, viewModel.uiState.value.currentTab)

    viewModel.setTab(BottomNavTab.LIVE_TV)
    assertEquals(BottomNavTab.LIVE_TV, viewModel.uiState.value.currentTab)

    viewModel.openTool(ToolScreen.SETTINGS)
    assertEquals(ToolScreen.SETTINGS, viewModel.uiState.value.activeToolScreen)

    viewModel.updateTheme(ThemeType.AMOLED)
    assertEquals(ThemeType.AMOLED, viewModel.uiState.value.settings.theme)

    viewModel.closeTool()
    assertEquals(null, viewModel.uiState.value.activeToolScreen)
  }

  @Test
  fun test_playback_and_favorites() {
    val viewModel = MainViewModel()
    val channel = viewModel.uiState.value.channels.first()

    viewModel.playChannel(channel)
    assertTrue(viewModel.uiState.value.isPlayerOpen)
    assertEquals(channel.name, viewModel.uiState.value.currentPlayingTitle)

    viewModel.toggleChannelFavorite(channel.id)
    val updated = viewModel.uiState.value.channels.first { it.id == channel.id }
    assertEquals(!channel.isFavorite, updated.isFavorite)
  }

  @Test
  fun test_ambient_themes_flow() {
    val viewModel = MainViewModel()
    assertTrue(viewModel.uiState.value.ambientThemes.isNotEmpty())

    val fireTheme = viewModel.uiState.value.ambientThemes.find { it.id == "fire" }!!
    viewModel.selectAmbientTheme(fireTheme)
    assertEquals("fire", viewModel.uiState.value.selectedAmbientTheme.id)

    viewModel.toggleAmbientVideo(false)
    assertEquals(false, viewModel.uiState.value.isAmbientVideoEnabled)
  }
}
