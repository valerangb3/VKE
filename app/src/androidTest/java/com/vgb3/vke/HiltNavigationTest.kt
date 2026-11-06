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
        waitForText("Гильдия Героев")
        composeRule.onAllNodesWithText("Т-Банк").onFirst().performClick()

        waitForText("Финансы")
        composeRule.onNodeWithText("Финансы").assertIsDisplayed()

        composeRule.activityRule.scenario.recreate()

        waitForText("Финансы")
        composeRule.onNodeWithText("Финансы").assertIsDisplayed()

        composeRule.runOnUiThread {
            composeRule.activity.onBackPressedDispatcher.onBackPressed()
        }

        waitForText("Гильдия Героев")
        composeRule.onNodeWithText("Гильдия Героев").assertIsDisplayed()
    }

    private fun waitForText(text: String) {
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
