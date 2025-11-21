package com.ourc.spacedodger.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GameViewModelTest {

    // (Opcional) Usar un dispatcher de prueba para controlar el tiempo en coroutines.
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: GameViewModel

    @Before
    fun setUp() {
        // Configura el dispatcher principal para que sea el de prueba.
        Dispatchers.setMain(testDispatcher)
        viewModel = GameViewModel() // Instancia el ViewModel.
    }

    @After
    fun tearDown() {
        // Limpia el dispatcher principal después de cada prueba.
        Dispatchers.resetMain()
    }

    @Test
    fun testMovePlayer() = runTest {
        // La librería Turbine nos permite "escuchar" los cambios en el Flow.
        viewModel.gameState.test {
            // 1. El estado inicial debe tener la posición por defecto.
            val initialState = awaitItem()
            assertThat(initialState.playerX).isEqualTo(0.5f) // Suponiendo que empieza en el centro.

            // 2. Llamamos a la función que queremos probar.
            viewModel.movePlayer(0.25f)

            // 3. Verificamos que se emitió un nuevo estado con la posición actualizada.
            val updatedState = awaitItem()
            assertThat(updatedState.playerX).isEqualTo(0.25f)

            // 4. Se pueden probar más movimientos.
            viewModel.movePlayer(0.8f)
            assertThat(awaitItem().playerX).isEqualTo(0.8f)

            // Cancelar la recolección del Flow para que la prueba termine.
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testTooglePause() = runTest {
        viewModel.gameState.test {
            // Estado inicial, suponemos que no está pausado.
            assertThat(awaitItem().isPaused).isFalse()

            // Primera llamada: debería pausar el juego.
            viewModel.togglePause()
            assertThat(awaitItem().isPaused).isTrue()

            // Segunda llamada: debería reanudar el juego.
            viewModel.togglePause()
            assertThat(awaitItem().isPaused).isFalse()

            cancelAndIgnoreRemainingEvents()
        }
    }
}