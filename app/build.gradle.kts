plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.tototo.video_community"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.tototo.video_community"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // --- Android Core ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // --- Compose BOM ---
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // --- Material 3 & Icons ---
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)  // 注意：这里应使用 androidx-compose-material-icons-extended

    // --- Lifecycle & ViewModel ---
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // --- Navigation ---
    implementation(libs.androidx.navigation.compose)

    // --- Koin (DI) ---
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // --- Coil 3 (图片加载) ---
    implementation(libs.bundles.coil)


    implementation(libs.kotlinx.serialization.json)

    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)

    // --- OkHttp (Coil 网络底层) ---
    // 已由 coil-network-okhttp 传递依赖，无需显式添加
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.compose.animation.core)

    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}