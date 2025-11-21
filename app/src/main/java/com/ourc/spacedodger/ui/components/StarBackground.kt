package com.ourc.spacedodger.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

/**
 * StarBackground crea un campo de estrellas animado.
 *
 * Las estrellas se mueven de arriba hacia abajo para dar
 * la sensación de que la nave está avanzando por el espacio.
 *
 * Es como ver las estrellas pasar por la ventana de un tren,
 * pero en el espacio.
 *
 * Usamos animación infinita para que nunca se detenga.
 */
@Composable
fun StarBackground() {
    // Crear animación infinita que nunca se detiene
    val infiniteTransition = rememberInfiniteTransition(label = "star_animation")

    // Animación del desplazamiento vertical
    // Va de 0 a 1000 y se repite indefinidamente
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 20000, // 20 segundos para un ciclo completo
                easing = LinearEasing    // Velocidad constante
            ),
            repeatMode = RepeatMode.Restart // Reiniciar al terminar
        ),
        label = "offset_y"
    )

    // Generar posiciones de estrellas fijas (no cambian)
    // Usamos remember para que las estrellas mantengan su posición
    val stars = remember {
        List(150) { index ->
            StarData(
                id = index,
                x = Random.nextFloat(),        // Posición X aleatoria (0.0 a 1.0)
                y = Random.nextFloat(),        // Posición Y aleatoria (0.0 a 1.0)
                size = Random.nextFloat() * 2f + 1f, // Tamaño (1 a 3 píxeles)
                brightness = Random.nextFloat() * 0.5f + 0.5f, // Brillo (0.5 a 1.0)
                speed = Random.nextFloat() * 0.5f + 0.5f // Velocidad relativa
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Dibujar cada estrella
        stars.forEach { star ->
            // Calcular posición con efecto parallax
            // Las estrellas más rápidas crean sensación de profundidad
            val x = star.x * width
            val y = ((star.y * height + offsetY * star.speed) % height)

            // Color de la estrella con brillo variable
            val starColor = Color.White.copy(alpha = star.brightness)

            // Dibujar la estrella como un círculo pequeño
            drawCircle(
                color = starColor,
                radius = star.size,
                center = Offset(x, y)
            )

            // Agregar un pequeño brillo (opcional)
            // Es como un destello de luz
            if (star.size > 2f) {
                drawCircle(
                    color = starColor.copy(alpha = star.brightness * 0.3f),
                    radius = star.size * 1.5f,
                    center = Offset(x, y)
                )
            }
        }

        // Dibujar algunas "estrellas fugaces" ocasionales
        // Son líneas que cruzan rápidamente
        if (offsetY.toInt() % 500 < 50) {
            val shootingStarX = (offsetY % width)
            val shootingStarY = height * 0.3f

            drawLine(
                color = Color.White.copy(alpha = 0.6f),
                start = Offset(shootingStarX, shootingStarY),
                end = Offset(shootingStarX + 30f, shootingStarY + 30f),
                strokeWidth = 2f
            )
        }
    }
}

/**
 * Clase de datos para representar una estrella individual.
 * Cada estrella tiene propiedades únicas que la hacen diferente.
 *
 * @property id Identificador único
 * @property x Posición horizontal (0.0 a 1.0)
 * @property y Posición vertical (0.0 a 1.0)
 * @property size Tamaño en píxeles
 * @property brightness Nivel de brillo (0.0 a 1.0)
 * @property speed Velocidad relativa del parallax
 */
private data class StarData(
    val id: Int,
    val x: Float,
    val y: Float,
    val size: Float,
    val brightness: Float,
    val speed: Float
)