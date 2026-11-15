package com.vgb3.vke

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vgb3.vke.domain.model.App
import com.vgb3.vke.presentation.applist.AppListUiState
import com.vgb3.vke.presentation.applist.ListScreen
import com.vgb3.vke.ui.theme.VKETheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RatingsScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun ratingsAndTimingsAreVisibleAndBothButtonsForwardClicks() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        var sequentialClicks = 0
        var parallelClicks = 0
        var state by mutableStateOf(
            AppListUiState.Content(
                apps = listOf(App("note", "QuickNote", "Заметки", "")),
                ratings = mapOf("note" to 4.4f),
                sequentialDurationMs = 18_000,
                parallelDurationMs = 1500,
            )
        )
        composeRule.setContent {
            VKETheme {
                ListScreen(
                    state = state,
                    snackbarHostState = remember { SnackbarHostState() },
                    onToolbarClick = {}, onBackClick = {}, onItemClick = {}, onInstallClick = {},
                    onSequentialRatingsClick = { sequentialClicks++ },
                    onParallelRatingsClick = { parallelClicks++ },
                )
            }
        }
        composeRule.onNodeWithText(context.getString(R.string.app_rating, 4.4f)).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.ratings_sequential_duration, 18_000L)).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.ratings_parallel_duration, 1500L)).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.ratings_sequential)).performClick()
        composeRule.onNodeWithText(context.getString(R.string.ratings_parallel)).performClick()
        composeRule.runOnIdle {
            assertEquals(1, sequentialClicks)
            assertEquals(1, parallelClicks)
            state = state.copy(ratingsLoading = true)
        }
        composeRule.onNodeWithText(context.getString(R.string.ratings_sequential)).assertIsNotEnabled()
        composeRule.onNodeWithText(context.getString(R.string.ratings_parallel)).assertIsNotEnabled()
    }
}
