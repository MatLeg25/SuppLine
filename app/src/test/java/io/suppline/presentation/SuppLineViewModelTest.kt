package io.suppline.presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.suppline.data.preferences.DefaultPreferences
import io.suppline.domain.models.Supplement
import io.suppline.domain.preferences.Preferences
import io.suppline.presentation.states.SuppLineState
import org.junit.Before
import java.time.LocalTime

//todo update test
class SuppLineViewModelTest() {

    lateinit var preferences: Preferences
    lateinit var viewModel: SuppLineViewModel

    private val DEFAULT_SUPPLEMENTS = listOf(
        Supplement(
            id = 1,
            name = "One",
            description = "description-One",
            consumed = false,
            timeToConsume = LocalTime.of(11,0),
            hasNotification = false
        ),
        Supplement(
            id = 2,
            name = "Two",
            description = "description-Two",
            consumed = false,
            timeToConsume = LocalTime.of(12,0),
            hasNotification = false
        ),
        Supplement(
            id = 3,
            name = "Three",
            description = "description-Three",
            consumed = false,
            timeToConsume = LocalTime.of(13,0),
            hasNotification = false
        )
    )

    @Before
    fun setup() {
        viewModel = mockk<SuppLineViewModel>()
        every { viewModel.state.value } returns SuppLineState(supplements = DEFAULT_SUPPLEMENTS)
    }

}