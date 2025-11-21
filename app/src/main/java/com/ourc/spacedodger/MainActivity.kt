package com.ourc.spacedodger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ourc.spacedodger.ui.screens.GameScreen
import com.ourc.spacedodger.ui.theme.SpaceDodgerTheme

/**
 * MainActivity es el punto de entrada de nuestra aplicación.
 *
 * Es como la puerta principal de una casa: todo empieza aquí.
 * Android llama a esta Activity cuando el usuario abre la app.
 *
 * ComponentActivity es una Activity que tiene soporte para Jetpack Compose.
 * Es la versión moderna y mejorada de Activity.
 */
class MainActivity : ComponentActivity() {

    /**
     * onCreate se llama cuando la Activity es creada.
     * Es como el constructor de una clase, pero para Activities.
     * libs
     * Aquí configuramos toda la interfaz de usuario.
     *
     * @param savedInstanceState Estado guardado de una ejecución anterior
     *                          (null si es la primera vez)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContent define el contenido de la pantalla usando Compose
        // Es como decir "esto es lo que el usuario verá"
        setContent {
            // SpaceDodgerTheme aplica el tema de colores de nuestra app
            SpaceDodgerTheme {
                // Surface es un contenedor con color de fondo
                // Es como un lienzo sobre el que pintamos
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // GameScreen es nuestra pantalla principal del juego
                    GameScreen()
                }
            }
        }
    }
}