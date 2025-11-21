plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.ourc.spacedodger"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ourc.spacedodger"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

// Reemplaza TODO tu bloque de dependencias con este bloque corregido

dependencies {
    // --- DEPENDENCIAS PRINCIPALES DE LA APP ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom)) // BOM para alinear versiones de Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // ViewModel para Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // --- DEPENDENCIAS PARA PRUEBAS UNITARIAS LOCALES (src/test) ---
    // Para probar la lógica del ViewModel
    testImplementation(libs.junit) // Usa la versión del catálogo, probablemente "junit:junit:4.13.2"
    testImplementation("app.cash.turbine:turbine:1.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation("com.google.truth:truth:1.4.2")

    // --- DEPENDENCIAS PARA PRUEBAS DE INSTRUMENTACIÓN/UI (src/androidTest) ---
    // Para probar la UI de GameScreen
    androidTestImplementation(platform(libs.androidx.compose.bom)) // BOM para alinear versiones de Compose en pruebas
    androidTestImplementation(libs.androidx.junit) // Runner para pruebas de Android
    androidTestImplementation(libs.androidx.espresso.core) // Espresso Core (aunque no lo uses directamente, es estándar)

    // ¡¡ESTAS SON LAS CLAVE PARA TU PROBLEMA!!
    // Resuelve createComposeRule y las funciones de prueba de Compose
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    // Resuelve mockk y verify para pruebas en Android
    androidTestImplementation("io.mockk:mockk-android:1.13.11")

    // --- DEPENDENCIAS DE DEPURACIÓN ---
    // Necesarias para que las pruebas de UI se puedan ejecutar y depurar
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
