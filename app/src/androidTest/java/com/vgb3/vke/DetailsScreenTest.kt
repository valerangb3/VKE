package com.vgb3.vke

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vgb3.vke.domain.model.AppDetails
import com.vgb3.vke.presentation.details.DetailsScreen
import com.vgb3.vke.presentation.details.DetailsUiState
import com.vgb3.vke.ui.theme.VKETheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun retryButtonCanReplaceErrorWithLoadedCard() {
        var state by mutableStateOf<DetailsUiState>(DetailsUiState.Error)
        composeRule.setContent {
            VKETheme {
                DetailsScreen(
                    state = state,
                    onBackClick = {},
                    onRetryClick = { state = DetailsUiState.Content(details, emptyList()) },
                )
            }
        }

        composeRule.onNodeWithText(context.getString(R.string.app_details_load_error)).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.app_list_retry)).performClick()

        composeRule.onNodeWithText("QuickNote").assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.app_details_load_error)).assertDoesNotExist()
    }

    @Test
    fun cardShowsPreciseSizeAndAllowsDescriptionToBeExpandedAndCollapsed() {
        composeRule.setContent {
            VKETheme {
                DetailsScreen(
                    state = DetailsUiState.Content(details, emptyList()),
                    onBackClick = {},
                )
            }
        }

        composeRule.onNodeWithText(context.getString(R.string.app_details_size_value, 8.2)).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.app_details_read_more))
            .performScrollTo().performClick()
        composeRule.onNodeWithText(context.getString(R.string.app_details_read_less))
            .performScrollTo().assertIsDisplayed().performClick()
        composeRule.onNodeWithText(context.getString(R.string.app_details_read_more))
            .performScrollTo().assertIsDisplayed()
    }

    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    private val details = AppDetails(
        id = "fa2e31b8-1234-4cf7-9914-108a170a1b01",
        name = "QuickNote",
        developer = "QuickSoft Inc.",
        category = "Производительность",
        age = 3,
        appSize = 8.2,
        screenshotItems = emptyList(),
        description = "QuickNote помогает записывать и находить идеи. ".repeat(20),
        iconApp = "",
    )
}
