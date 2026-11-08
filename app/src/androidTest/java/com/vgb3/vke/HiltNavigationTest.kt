package com.vgb3.vke

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HiltNavigationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun injectedViewModelsLoadDataAndRestoreDetailsAfterRecreation() {
        waitForText("QuickNote")
        composeRule.onAllNodesWithText("QuickNote").onFirst().performClick()

        // Экран загрузился через Hilt и assisted-фабрику с appId из Navigation 3.
        waitForText("QuickSoft Inc.")
        composeRule.onAllNodesWithText("QuickSoft Inc.").onFirst().assertIsDisplayed()

        composeRule.activityRule.scenario.recreate()

        waitForText("QuickSoft Inc.")
        composeRule.onAllNodesWithText("QuickSoft Inc.").onFirst().assertIsDisplayed()

        composeRule.runOnUiThread {
            composeRule.activity.onBackPressedDispatcher.onBackPressed()
        }

        waitForText("Приложения")
        composeRule.onNodeWithText("QuickNote").assertIsDisplayed()
        composeRule.onNodeWithText("Calmalist").performClick()

        waitForText("Calma Studios")
        composeRule.onNodeWithText("Calmalist").assertIsDisplayed()
    }

    private fun waitForText(text: String) {
        composeRule.waitUntil(timeoutMillis = 35_000) {
            composeRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
