package io.suppline.presentation


import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.suppline.R
import io.suppline.data.FakePreferences
import io.suppline.domain.useCase.GetDailySupplementationUseCase
import io.suppline.domain.useCase.SaveDailySupplementationUseCase
import io.suppline.presentation.components.ActionButton
import io.suppline.presentation.ui.theme.SuppLineTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SuppLineE2E {

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
        viewModelFake = SuppLineViewModel(
            getSupplementation = GetDailySupplementationUseCase(preferencesFake),
            saveSupplementation = SaveDailySupplementationUseCase(preferencesFake),
        )
    }

    @Test
    fun firstTest() {
        //top logo isDisplayed
        composeRule.onNodeWithText(text = getString(R.string.suppline_title)).isDisplayed()
        //check AddEdit modal
        composeRule.onNodeWithText(text = getString(R.string.add_supplement)).performClick()
        composeRule.onNodeWithTag(getString(R.string.add)).isDisplayed()
        composeRule.onNodeWithTag(getString(R.string.cancel)).isDisplayed()
        composeRule.onNodeWithTag(getString(R.string.remove)).isDisplayed()
        //
        assert(1 == 1)
    }

    private fun getString(@StringRes stringRes: Int): String {
        return composeRule.activity.getString(stringRes)
    }

}