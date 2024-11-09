package io.suppline.presentation


import androidx.annotation.StringRes
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import io.suppline.R
import io.suppline.data.preferences.FakePreferences
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
    val composeRuleActivity = createAndroidComposeRule<MainActivity>()
    @get:Rule
    val composeRuleScreen = createComposeRule()

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
        composeRuleScreen.setContent {
            SuppLineScreen(viewModel = viewModelFake)
        }
    }

    @Test
    fun add_and_delete_items_correctly_update_ui_and_preferences() {
        //check if preferences and viewmodel state has empty supplement list
        assert(preferencesFake.loadDailySupplements()?.supplements?.isEmpty() ?: true)
        assert(viewModelFake.state.value.supplements.isEmpty())
        //top logo isDisplayed
        composeRuleScreen.onNodeWithText(text = getString(R.string.suppline_title)).isDisplayed()
        //check AddEdit modal
        composeRuleScreen.onNodeWithText(text = getString(R.string.add_supplement)).performClick()
        composeRuleScreen.onNodeWithTag(getString(R.string.add)).isDisplayed()
        composeRuleScreen.onNodeWithTag(getString(R.string.cancel)).isDisplayed()
        composeRuleScreen.onNodeWithTag(getString(R.string.remove)).isDisplayed()

        //prevent adding item with empty name
        composeRuleScreen.onNodeWithText(getString(R.string.name)).performTextInput("")
        composeRuleScreen.onNodeWithTag(getString(R.string.add)).performClick()
        composeRuleScreen.onNodeWithTag(getString(R.string.add)).isDisplayed()

        //add new item with [sampleName]
        composeRuleScreen.onNodeWithText(getString(R.string.name)).performTextInput(sampleName)
        composeRuleScreen.onNodeWithTag(getString(R.string.add)).performClick()
        //close AddEdit item modal
        composeRuleScreen.onNodeWithTag(getString(R.string.add)).isNotDisplayed()
        //check if preferences and viewmodel state has updated supplement list
        val preferencesItem = preferencesFake.loadDailySupplements()?.supplements?.count { it.name == sampleName } ?: -1
        val viewModelItem = viewModelFake.state.value.supplements.count { it.name == sampleName }
        assert(preferencesItem == viewModelItem && viewModelItem == 1)

        //add new item with [item2Name]
        val item2Name = "$sampleName-2"
        composeRuleScreen.onNodeWithText(text = getString(R.string.add_supplement)).performClick()
        composeRuleScreen.onNodeWithText(getString(R.string.name)).performTextInput(item2Name)
        //put description
        composeRuleScreen.onNodeWithTag(testTag = getString(R.string.description)).performClick()
        composeRuleScreen.onNodeWithTag(testTag = getString(R.string.description)).performTextInput(sampleDescription)
        composeRuleScreen.onNodeWithTag(getString(R.string.add)).performClick()
        //close AddEdit item modal
        composeRuleScreen.onNodeWithTag(getString(R.string.add)).isNotDisplayed()
        //check if preferences and viewmodel state has updated supplement list
        val preferencesItem2 = preferencesFake.loadDailySupplements()?.supplements?.count { it.description == sampleDescription } ?: -1
        val viewModelItem2 = viewModelFake.state.value.supplements.count { it.description == sampleDescription }
        assert(preferencesItem2 == viewModelItem2 && viewModelItem2 == 1)
        val totalItems = preferencesFake.loadDailySupplements()?.supplements?.size
        val totalItemsVM = viewModelFake.state.value.supplements.size
        assert(totalItems == totalItemsVM && totalItemsVM == 2)
    }

    private fun getString(@StringRes stringRes: Int): String {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        return context.getString(stringRes)
    }

}