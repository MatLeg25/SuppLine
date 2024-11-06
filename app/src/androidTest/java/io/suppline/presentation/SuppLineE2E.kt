package io.suppline.presentation


import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import io.suppline.data.FakePreferences
import io.suppline.domain.useCase.GetDailySupplementationUseCase
import io.suppline.domain.useCase.SaveDailySupplementationUseCase
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

        composeRule.activity.setContent {
            SuppLineTheme {

            }
        }
    }

    @Test
    fun firstTest() {
        assert(1 == 1)
    }

    private fun getString(@StringRes stringRes: Int): String {
        return composeRule.activity.getString(stringRes)
    }

}