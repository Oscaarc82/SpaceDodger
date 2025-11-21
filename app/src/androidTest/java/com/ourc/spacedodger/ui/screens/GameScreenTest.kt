package com.ourc.spacedodger.ui.screens

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.ourc.spacedodger.viewmodel.GameViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class GameScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Usaremos un ViewModel "mock" (falso) para aislar la UI.
    private val mockViewModel: GameViewModel = mockk(relaxed = true)

    @Test
    fun clickTogglePauseDelViewModel() {
        // 1. Renderizamos la pantalla GameScreen, pasándole nuestro ViewModel falso.
        composeTestRule.setContent {
            GameScreen(viewModel = mockViewModel)
        }

        // 2. Buscamos un nodo (componente) que contenga el texto "Pausa".
        //    (Nota: esto asume que tu GameHUD tiene un botón con ese texto visible).
        //    Si usas un Icono, deberías usar un `testTag`.
        composeTestRule.onNodeWithText("Pausa", useUnmergedTree = true).performClick()

        // 3. Verificamos que la función `togglePause()` del ViewModel fue llamada exactamente una vez.
        verify(exactly = 1) { mockViewModel.togglePause() }
    }
}