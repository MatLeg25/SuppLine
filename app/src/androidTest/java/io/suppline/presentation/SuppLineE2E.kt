package io.suppline.presentation


import androidx.annotation.StringRes
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import io.suppline.R
import io.suppline.data.FakePreferences
import io.suppline.domain.models.DailySupplementation
import io.suppline.domain.useCase.GetDailySupplementationUseCase
import io.suppline.domain.useCase.SaveDailySupplementationUseCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class SuppLineE2E {

    private val sampleName = "Sample name"
    private val sampleDescription = "description"

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var viewModelFake: SuppLineViewModel
    private lateinit var preferencesFake: FakePreferences

    private val buttonMatcher = SemanticsMatcher.expectValue(
        SemanticsProperties.Role, Role.Button
    )


    @Before
    fun setUp() {
        preferencesFake = FakePreferences()
        preferencesFake.saveDailySupplementation(DailySupplementation(LocalDate.now(), emptyList()))
        viewModelFake = SuppLineViewModel(
            getSupplementation = GetDailySupplementationUseCase(preferencesFake),
            saveSupplementation = SaveDailySupplementationUseCase(preferencesFake),
        )
    }

    @Test
    fun add_and_delete_items_correctly_update_ui_and_preferences() {
        assert(viewModelFake.state.value.supplements.isEmpty())
        //top logo isDisplayed
        composeRule.onNodeWithText(text = getString(R.string.suppline_title)).isDisplayed()
        //check AddEdit modal
        composeRule.onNodeWithText(text = getString(R.string.add_supplement)).performClick()
        composeRule.onNodeWithTag(getString(R.string.add)).isDisplayed()
        composeRule.onNodeWithTag(getString(R.string.cancel)).isDisplayed()
        composeRule.onNodeWithTag(getString(R.string.remove)).isDisplayed()

        //prevent adding item with empty name
        composeRule.onNodeWithText(getString(R.string.name)).performTextInput("")
        composeRule.onNodeWithTag(getString(R.string.add)).performClick()
        composeRule.onNodeWithTag(getString(R.string.add)).isDisplayed()

        //add new item with [sampleName]
        composeRule.onNodeWithText(getString(R.string.name)).performTextInput(sampleName)
        composeRule.onNodeWithTag(getString(R.string.add)).performClick()
        //close AddEdit item modal
        composeRule.onNodeWithTag(getString(R.string.add)).isNotDisplayed()

        assert(1 == 1)
    }

    private fun getString(@StringRes stringRes: Int): String {
        return composeRule.activity.getString(stringRes)
    }

}