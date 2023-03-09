plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.lelestacia.lelenimecompose"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.lelestacia.lelenimecompose"
        minSdk = 24
        targetSdk = 33
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
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packagingOptions {
        resources {
            exclude("/META-INF/LICENSE")
            exclude("/META-INF/LICENSE.txt")
            exclude("/META-INF/NOTICE")
            exclude("/META-INF/NOTICE.txt")
            exclude("/META-INF/DEPENDENCIES")
            exclude("/META-INF/*.kotlin_module")
        }
    }
}

dependencies {
    //  Module Implementation
    implementation(project(":core:common"))
    implementation(project(":feature:explore"))
    implementation(project(":feature:collection"))
    implementation(project(":feature:more"))
    implementation(project(":feature:detail"))

    //  Compose Toolkit
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.compose.navigation)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.junit)
    debugImplementation(libs.bundles.compose.tooling.and.manifest)

    // KTX
    implementation(libs.kotlin.ktx)

    // Lifecycle
    implementation(libs.lifecycle.runtime)

    //  Activity Compose
    implementation(libs.activity.compose)

    // Android JUnit
    androidTestImplementation(libs.android.junit)

    // Junit
    testImplementation(libs.junit)

    // Espresso UI Test
    androidTestImplementation(libs.espresso)

    //  Dagger Hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    //  Timber
    implementation(libs.timber)
}

kapt {
    correctErrorTypes = true
    showProcessorStats = true
    useBuildCache = true
}
