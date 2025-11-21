package com.ourc.spacedodger.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ourc.spacedodger.data.Asteroid
import kotlin.math.cos
import kotlin.math.sin

/**
 * AsteroidView dibuja un asteroide individual.
 *
 * Los asteroides son rocas espaciales con forma irregular
 * que caen desde arriba. Cada uno tiene:
 * - Forma ligeramente diferente (usando matemáticas)
 * - Cráteres de impacto
 * - Tamaño variable
 *
 * Es como dibujar una piedra con textura,
 * pero cada piedra es única.
 *
 * @param asteroid El objeto Asteroid con posición y propiedades
 */
@Composable
fun AsteroidView(asteroid: Asteroid) {
    BoxWithConstraints {
        // Convertir posición relativa a píxeles
        val xPixels = asteroid.x * constraints.maxWidth.toFloat()
        val yPixels = asteroid.y * constraints.maxHeight.toFloat()

        // Tamaño base del asteroide (ajustado por su propiedad size)
        val baseSize = 40.dp
        val scaledSize = baseSize * asteroid.size

        Canvas(
            modifier = Modifier
                .offset {
                    IntOffset(
                        (xPixels - (scaledSize.toPx() / 2)).toInt(),
                        (yPixels - (scaledSize.toPx() / 2)).toInt()
                    )
                }
                .size(scaledSize)
        ) {
            val radius = size.width / 2
            val center = Offset(size.width / 2, size.height / 2)

            // 1. FORMA BASE DEL ASTEROIDE
            // Dibujar múltiples círculos irregulares para dar textura
            // Es como apilar círculos de diferentes tamaños

            // Círculo principal (el más grande)
            drawCircle(
                color = Color(0xFF4A4A4A), // Gris oscuro
                radius = radius,
                center = center
            )

            // Crear forma irregular con círculos adicionales
            // Usamos el ID del asteroide como semilla para que cada uno sea único
            val seed = asteroid.id
            for (i in 0..5) {
                val angle = (i * 60f + seed * 15) * Math.PI / 180f
                val distance = radius * 0.3f
                val offsetX = (cos(angle) * distance).toFloat()
                val offsetY = (sin(angle) * distance).toFloat()

                drawCircle(
                    color = Color(0xFF3A3A3A), // Gris más oscuro
                    radius = radius * 0.4f,
                    center = Offset(
                        center.x + offsetX,
                        center.y + offsetY
                    )
                )
            }

            // 2. CRÁTERES DE IMPACTO
            // Dibujar círculos más pequeños y oscuros como cráteres
            // Es como hacer agujeros en una pelota de plastilina

            // Cráter grande central
            drawCircle(
                color = Color(0xFF2A2A2A),
                radius = radius * 0.25f,
                center = Offset(
                    center.x + radius * 0.2f,
                    center.y - radius * 0.15f
                )
            )

            // Cráter mediano
            drawCircle(
                color = Color(0xFF2A2A2A),
                radius = radius * 0.18f,
                center = Offset(
                    center.x - radius * 0.25f,
                    center.y + radius * 0.1f
                )
            )

            // Cráter pequeño
            drawCircle(
                color = Color(0xFF2A2A2A),
                radius = radius * 0.12f,
                center = Offset(
                    center.x + radius * 0.1f,
                    center.y + radius * 0.3f
                )
            )

            // 3. DETALLES Y TEXTURAS
            // Agregar pequeños puntos como detalles de superficie

            // Puntos claros (como minerales brillantes)
            for (i in 0..8) {
                val angle = (i * 40f + seed * 20) * Math.PI / 180f
                val distance = radius * (0.4f + (i % 3) * 0.15f)
                val offsetX = (cos(angle) * distance).toFloat()
                val offsetY = (sin(angle) * distance).toFloat()

                drawCircle(
                    color = Color(0xFF5A5A5A), // Gris más claro
                    radius = radius * 0.08f,
                    center = Offset(
                        center.x + offsetX,
                        center.y + offsetY
                    )
                )
            }

            // 4. BORDE PARA DEFINICIÓN
            // Un borde sutil para que se vea más definido contra el fondo
            drawCircle(
                color = Color(0xFF6A6A6A),
                radius = radius,
                center = center,
                style = Stroke(width = 1f)
            )

            // 5. SOMBRA INTERIOR (opcional)
            // Crear efecto 3D con un gradiente simulado
            drawCircle(
                color = Color.Black.copy(alpha = 0.2f),
                radius = radius * 0.9f,
                center = Offset(
                    center.x + radius * 0.1f,
                    center.y + radius * 0.1f
                )
            )
        }
    }
}