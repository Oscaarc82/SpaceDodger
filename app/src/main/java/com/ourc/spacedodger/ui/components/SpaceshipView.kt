package com.ourc.spacedodger.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

/**
 * SpaceshipView dibuja la nave espacial del jugador.
 *
 * La nave es un triángulo (como una flecha apuntando hacia arriba)
 * con una cabina circular y motores en la base.
 *
 * Es como dibujar con lápices de colores en un papel,
 * pero usando código en lugar de lápices.
 *
 * @param x Posición horizontal (0.0 = izquierda, 1.0 = derecha)
 * @param y Posición vertical (0.0 = arriba, 1.0 = abajo)
 */
@Composable
fun SpaceshipView(x: Float, y: Float) {
    BoxWithConstraints {
        // Convertir posición relativa (0-1) a píxeles reales
        val xPixels = x * constraints.maxWidth.toFloat()
        val yPixels = y * constraints.maxHeight.toFloat()

        // Tamaño de la nave
        val shipSize = 50.dp

        Canvas(
            modifier = Modifier
                .offset {
                    IntOffset(
                        (xPixels - 25.dp.toPx()).toInt(),
                        (yPixels - 25.dp.toPx()).toInt()
                    )
                }
                .size(shipSize)
        ) {
            val width = size.width
            val height = size.height

            // 1. CUERPO PRINCIPAL (Triángulo)
            // Es como dibujar un triángulo con tres puntos
            val bodyPath = Path().apply {
                moveTo(width / 2, 0f)           // Punta superior (nariz)
                lineTo(width * 0.2f, height)    // Esquina inferior izquierda
                lineTo(width * 0.8f, height)    // Esquina inferior derecha
                close()                          // Conectar al punto inicial
            }

            // Dibujar el cuerpo con relleno cyan
            drawPath(
                path = bodyPath,
                color = Color(0xFF00CED1) // Cyan oscuro
            )

            // Dibujar borde del cuerpo
            drawPath(
                path = bodyPath,
                color = Color(0xFF00FFFF), // Cyan brillante
                style = Stroke(width = 2f)
            )

            // 2. CABINA (Círculo en la parte superior)
            // La cabina del piloto, como una ventana
            drawCircle(
                color = Color(0xFF87CEEB), // Azul cielo
                radius = width / 8,
                center = Offset(width / 2, height * 0.35f)
            )

            // Borde de la cabina
            drawCircle(
                color = Color.White,
                radius = width / 8,
                center = Offset(width / 2, height * 0.35f),
                style = Stroke(width = 1.5f)
            )

            // 3. MOTORES (Dos rectángulos en la base)
            // Motor izquierdo
            drawRect(
                color = Color(0xFFFF6B35), // Naranja (fuego)
                topLeft = Offset(width * 0.25f, height * 0.85f),
                size = Size(width * 0.15f, height * 0.2f)
            )

            // Motor derecho
            drawRect(
                color = Color(0xFFFF6B35), // Naranja (fuego)
                topLeft = Offset(width * 0.6f, height * 0.85f),
                size = Size(width * 0.15f, height * 0.2f)
            )

            // 4. ALAS LATERALES
            // Ala izquierda
            val leftWingPath = Path().apply {
                moveTo(width * 0.2f, height * 0.6f)
                lineTo(0f, height * 0.8f)
                lineTo(width * 0.2f, height * 0.9f)
                close()
            }
            drawPath(
                path = leftWingPath,
                color = Color(0xFF008B8B) // Cyan más oscuro
            )

            // Ala derecha
            val rightWingPath = Path().apply {
                moveTo(width * 0.8f, height * 0.6f)
                lineTo(width, height * 0.8f)
                lineTo(width * 0.8f, height * 0.9f)
                close()
            }
            drawPath(
                path = rightWingPath,
                color = Color(0xFF008B8B) // Cyan más oscuro
            )

            // 5. DETALLES DECORATIVOS (líneas en el cuerpo)
            // Línea decorativa 1
            drawLine(
                color = Color(0xFF00FFFF),
                start = Offset(width / 2, height * 0.5f),
                end = Offset(width / 2, height * 0.7f),
                strokeWidth = 2f
            )

            // Líneas laterales
            drawLine(
                color = Color(0xFF00FFFF),
                start = Offset(width * 0.35f, height * 0.55f),
                end = Offset(width * 0.35f, height * 0.75f),
                strokeWidth = 1.5f
            )

            drawLine(
                color = Color(0xFF00FFFF),
                start = Offset(width * 0.65f, height * 0.55f),
                end = Offset(width * 0.65f, height * 0.75f),
                strokeWidth = 1.5f
            )
        }
    }
}