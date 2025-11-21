package com.ourc.spacedodger.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ourc.spacedodger.ui.components.SpaceshipView
import com.ourc.spacedodger.ui.components.StarBackground
import com.ourc.spacedodger.ui.components.AsteroidView
import com.ourc.spacedodger.ui.components.GameHUD
import com.ourc.spacedodger.viewmodel.GameViewModel

/**
 * GameScreen es la pantalla principal del juego.
 *
 * Esta es la "cocina" donde se juntan todos los ingredientes:
 * - El fondo de estrellas
 * - Los asteroides
 * - La nave del jugador
 * - El HUD con puntuación
 *
 * También maneja la entrada del usuario (toques y arrastres).
 *
 * Es como un director de orquesta que coordina a todos los músicos
 * para crear una sinfonía.
 */
@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    // Observar el estado del juego
    // collectAsState() hace que Compose se actualice automáticamente
    // cuando el estado cambia. Es como suscribirse a un canal de noticias.
    val gameState by viewModel.gameState.collectAsState()

    // Box permite apilar elementos uno sobre otro
    // Es como las capas de una hamburguesa
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Fondo negro del espacio
            .pointerInput(Unit) {
                // Detectar gestos de arrastre (deslizar el dedo)
                detectDragGestures { change, _ ->
                    // change.position.x es la posición X del toque en píxeles
                    // size.width es el ancho total de la pantalla
                    // Dividirlos nos da un valor entre 0.0 y 1.0
                    val normalizedX = change.position.x / size.width

                    // Mover el jugador a la nueva posición
                    viewModel.movePlayer(normalizedX)

                    // Consumir el evento para que no se propague
                    change.consume()
                }
            }
    ) {
        // CAPA 1: Fondo de estrellas animado
        // Es la capa más profunda, como el cielo
        StarBackground()

        // CAPA 2: Asteroides
        // Dibujar cada asteroide en su posición
        gameState.asteroids.forEach { asteroid ->
            AsteroidView(asteroid = asteroid)
        }

        // CAPA 3: Nave del jugador
        // Siempre visible, controlada por el usuario
        SpaceshipView(
            x = gameState.playerX,
            y = gameState.playerY
        )

        // CAPA 4: Interfaz de usuario (HUD)
        // La capa más superficial, siempre visible
        GameHUD(
            score = gameState.score,
            level = gameState.level,
            highScore = gameState.highScore,
            isPaused = gameState.isPaused,
            isGameOver = gameState.isGameOver,
            onPauseClick = { viewModel.togglePause() },
            onRestartClick = { viewModel.restartGame() }
        )
    }
}