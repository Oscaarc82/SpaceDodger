package com.ourc.spacedodger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ourc.spacedodger.data.Asteroid
import com.ourc.spacedodger.data.GameState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * GameViewModel es el cerebro del juego.
 * Maneja toda la lógica: movimiento, colisiones, puntuación, etc.
 *
 * Piensa en esto como el director de una orquesta:
 * - Coordina todos los elementos (nave, asteroides, puntuación)
 * - Mantiene el ritmo (game loop a 60 FPS)
 * - Responde a las acciones del usuario
 */
class GameViewModel : ViewModel() {

    // StateFlow es como un canal de televisión en vivo
    // Todos pueden ver el estado actual y recibir actualizaciones automáticamente
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    // Job del game loop - permite cancelarlo cuando sea necesario
    private var gameJob: Job? = null

    // Contador para IDs únicos de asteroides
    private var asteroidIdCounter = 0

    // Constantes del juego (como las reglas de un juego de mesa)
    private val baseAsteroidSpeed = 0.008f
    private val playerRadius = 0.04f
    private val asteroidBaseRadius = 0.05f

    init {
        // Cuando se crea el ViewModel, iniciamos el juego automáticamente
        startGame()
    }

    /**
     * Inicia el game loop principal.
     * Es como encender un motor que corre constantemente.
     * Se ejecuta 60 veces por segundo (60 FPS) para mantener el juego fluido.
     */
    fun startGame() {
        // Cancelar cualquier juego anterior que esté corriendo
        gameJob?.cancel()

        // Iniciar un nuevo juego en una coroutine
        gameJob = viewModelScope.launch {
            while (true) {
                // Esperar 16 milisegundos (1000ms / 60 = 16.66ms para 60 FPS)
                delay(16L)

                // Solo actualizar si el juego no está pausado ni terminado
                if (!_gameState.value.isPaused && !_gameState.value.isGameOver) {
                    updateGame()
                }
            }
        }
    }

    /**
     * Actualiza el estado del juego en cada frame.
     * Es como actualizar la posición de las piezas en un tablero de ajedrez.
     */
    private fun updateGame() {
        _gameState.update { currentState ->
            // 1. MOVER ASTEROIDES HACIA ABAJO
            // Cada asteroide se mueve según su velocidad
            val updatedAsteroids = currentState.asteroids
                .map { asteroid ->
                    asteroid.copy(y = asteroid.y + asteroid.speed)
                }
                .filter { it.y < 1.2f } // Eliminar asteroides que salieron de pantalla

            // 2. GENERAR NUEVOS ASTEROIDES
            // La probabilidad aumenta con el nivel (como subir la dificultad)
            val spawnChance = 0.02f + (currentState.level * 0.002f)
            val newAsteroids = if (Random.nextFloat() < spawnChance) {
                updatedAsteroids + createNewAsteroid(currentState.level)
            } else {
                updatedAsteroids
            }

            // 3. VERIFICAR COLISIONES
            val collision = checkCollision(
                currentState.playerX,
                currentState.playerY,
                newAsteroids
            )

            // 4. ACTUALIZAR PUNTUACIÓN
            // Incrementar puntuación si no hay colisión
            val newScore = if (!collision) currentState.score + 1 else currentState.score

            // 5. CALCULAR NIVEL
            // Subir de nivel cada 500 puntos
            val newLevel = (newScore / 500) + 1

            // 6. ACTUALIZAR HIGH SCORE
            val newHighScore = maxOf(currentState.highScore, newScore)

            // Retornar el nuevo estado actualizado
            currentState.copy(
                asteroids = newAsteroids,
                score = newScore,
                level = newLevel,
                highScore = newHighScore,
                isGameOver = collision
            )
        }
    }

    /**
     * Crea un nuevo asteroide con posición y velocidad aleatorias.
     * Es como lanzar una nueva pieza al tablero.
     *
     * @param level Nivel actual para ajustar dificultad
     * @return Un nuevo asteroide con propiedades aleatorias
     */
    private fun createNewAsteroid(level: Int): Asteroid {
        // Velocidad aumenta con el nivel
        val speedMultiplier = 1f + (level * 0.15f)

        return Asteroid(
            id = asteroidIdCounter++,
            x = Random.nextFloat(), // Posición X aleatoria (0.0 a 1.0)
            y = -0.1f, // Empieza arriba de la pantalla
            speed = (Random.nextFloat() * 0.005f + baseAsteroidSpeed) * speedMultiplier,
            size = Random.nextFloat() * 0.5f + 0.75f // Tamaño variable (0.75 a 1.25)
        )
    }

    /**
     * Verifica si hay colisión entre el jugador y algún asteroide.
     * Usa geometría básica: si la distancia entre centros es menor
     * que la suma de radios, hay colisión.
     *
     * Es como verificar si dos círculos se tocan en un papel.
     *
     * @param playerX Posición X del jugador
     * @param playerY Posición Y del jugador
     * @param asteroids Lista de asteroides a verificar
     * @return true si hay colisión, false si no
     */
    private fun checkCollision(
        playerX: Float,
        playerY: Float,
        asteroids: List<Asteroid>
    ): Boolean {
        return asteroids.any { asteroid ->
            // Calcular distancia entre centros usando teorema de Pitágoras
            val dx = playerX - asteroid.x
            val dy = playerY - asteroid.y
            val distance = sqrt(dx * dx + dy * dy)

            // Radio del asteroide varía según su tamaño
            val asteroidRadius = asteroidBaseRadius * asteroid.size

            // ¿La distancia es menor que la suma de radios?
            distance < (playerRadius + asteroidRadius)
        }
    }

    /**
     * Mueve el jugador a una nueva posición X.
     * Es como mover tu personaje en un videojuego.
     *
     * @param x Nueva posición X (0.0 a 1.0)
     */
    fun movePlayer(x: Float) {
        // Asegurar que x esté entre 0 y 1 (como poner rieles a un tren)
        val clampedX = x.coerceIn(0f, 1f)

        _gameState.update { currentState ->
            currentState.copy(playerX = clampedX)
        }
    }

    /**
     * Alterna el estado de pausa del juego.
     * Es como presionar el botón de pausa en un control remoto.
     */
    fun togglePause() {
        _gameState.update { currentState ->
            currentState.copy(isPaused = !currentState.isPaused)
        }
    }

    /**
     * Reinicia el juego a su estado inicial.
     * Es como presionar el botón de reset en una consola.
     */
    fun restartGame() {
        // Cancelar el juego actual
        gameJob?.cancel()

        // Resetear el estado manteniendo el high score
        val currentHighScore = _gameState.value.highScore
        _gameState.value = GameState(highScore = currentHighScore)

        // Reiniciar contador de asteroides
        asteroidIdCounter = 0

        // Iniciar nuevo juego
        startGame()
    }

    /**
     * Limpieza cuando el ViewModel es destruido.
     * Es como apagar las luces al salir de una habitación.
     */
    override fun onCleared() {
        super.onCleared()
        gameJob?.cancel()
    }
}