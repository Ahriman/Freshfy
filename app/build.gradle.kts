plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
//    kotlin("jvm") version "1.9.21"
//    kotlin("plugin.serialization") version "1.9.21"
//    kotlin("plugin.serialization") version "1.9.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20"
//    id("dagger.hilt.android.plugin")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.marcossan.despensa"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.marcossan.despensa"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
//        jvmTarget = "1.8"
        jvmTarget = "17"

    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Hilt
    implementation("com.google.dagger:hilt-android:2.49")
    ksp("com.google.dagger:hilt-compiler:2.49")

//    implementation("com.google.android.gms:play-services-vision-common:19.1.3")
//    implementation("com.google.android.gms:play-services-vision:20.1.3")
    implementation("androidx.camera:camera-core:1.3.0")
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")
    implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:18.3.0")
    implementation("com.google.android.gms:play-services-code-scanner:16.1.0")
    ksp("com.google.dagger:hilt-android-compiler:2.48.1")

    // Room start
    val roomVersion = "2.6.1"

    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$roomVersion")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")

    // optional - RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:$roomVersion")

    // optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:$roomVersion")

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:$roomVersion")

    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$roomVersion")

    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:$roomVersion")

    // Room end

    // Navigation start
    val navVersion = "2.7.5"

    // Java language implementation
//    implementation("androidx.navigation:navigation-fragment:$navVersion")
//    implementation("androidx.navigation:navigation-ui:$navVersion")

    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$navVersion")

    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$navVersion")

    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:$navVersion") // Mínimo instalar esta
    // Navigation end

    // Google Vision
//    implementation("com.google.android.gms:play-services-code-scanner:16.1.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
//    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // Inicio App Retrofit
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Retrofit with Scalar Converter
    //implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    // Retrofit with Kotlin serialization Converter
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Moshi
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")


    // Kotlin serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.21")


    // Coil for images of internet
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // Import the Compose BOM
//    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
//    implementation("androidx.activity:activity-compose:1.8.0")
//    implementation("androidx.compose.material3:material3:1.1.2")
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.core:core-ktx:1.12.0")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    // Fin


//    implementation("androidx.activity:activity-ktx:1.8.1")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

//    implementation("com.google.firebase:firebase-ml-vision:28.2.1")
//    implementation("com.google.firebase:firebase-ml-vision-barcode-model:16.1.2")
//
//    implementation("com.google.android.gms:play-services-vision:20.1.3")
//    implementation("com.google.android.gms:play-services-vision-common:19.1.3")
    implementation("com.google.mlkit:barcode-scanning:17.2.0")

    // Use this dependency to bundle the model with your app
//    implementation("com.google.mlkit:barcode-scanning:17.2.0")
//
//
//
//    implementation("androidx.camera:camera-core:1.3.0")
//    implementation("androidx.camera:camera-camera2:1.3.0")
//    implementation("androidx.camera:camera-lifecycle:1.3.0")


    // ML Kit
    implementation("com.google.mlkit:barcode-scanning:17.2.0")

    // CameraX
    val cameraxVersion = "1.4.0-alpha02"
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")

    // Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")



    implementation("com.google.android.gms:play-services-vision:20.1.3")
    implementation("com.google.android.gms:play-services-auth:20.7.0")


    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.compose.foundation:foundation:1.5.4")
    implementation("androidx.compose.material:material:1.5.4")
    implementation("androidx.compose.runtime:runtime:1.5.4")
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.ui:ui-tooling:1.5.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
//    implementation("com.google.android.gms:play-services-vision:23.0.0") // Para Google Vision API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
}