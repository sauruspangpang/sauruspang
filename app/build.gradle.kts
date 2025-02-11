import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
}

// ✅ local.properties에서 API 키 가져오기
val localProps = Properties()
val localFile = rootProject.file("local.properties")

if (localFile.exists()) {
    FileInputStream(localFile).use { localProps.load(it) } // 🔥 안정적인 방식으로 수정
}

val openAiKey = localProps.getProperty("MY_API_KEY", "")

android {
    namespace = "com.ksj.sauruspang"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ksj.sauruspang"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // ✅ local.properties에 저장한 API 키를 BuildConfig.API_KEY로 설정
        buildConfigField("String", "API_KEY", "\"$openAiKey\"")

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
        buildConfig = true  // ✅ 반드시 활성화해야 BuildConfig.API_KEY 사용 가능
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.common)
    implementation(libs.digital.ink.recognition)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // ✅ 이미지 로딩 라이브러리
    implementation("io.coil-kt:coil-compose:2.3.0")

    // ✅ Google ML Kit (필기 인식)
    implementation("com.google.mlkit:digital-ink-recognition:18.1.0")

    // ✅ 코루틴 (비동기 처리)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")

    // ✅ Lottie 애니메이션
    implementation("com.airbnb.android:lottie-compose:6.6.2")

    // ✅ 권한 요청 라이브러리
    implementation(libs.accompanist.permissions)

    // ✅ Room Database (로컬 DB)
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    // ✅ CameraX (카메라 기능)
    val cameraxVersion = "1.3.1"
    implementation("androidx.camera:camera-core:${cameraxVersion}")
    implementation("androidx.camera:camera-camera2:${cameraxVersion}")
    implementation("androidx.camera:camera-view:${cameraxVersion}")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")

    // ✅ 네트워크 요청 (OkHttp & Retrofit)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // ✅ Fragment & Activity KTX
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    implementation("androidx.activity:activity-ktx:1.10.0")

    // Gson (JSON 파싱용)
    implementation("com.google.code.gson:gson:2.10.1")

    // Compose 관련 라이브러리 (mutableStateOf 오류 해결)
    implementation("androidx.compose.runtime:runtime:1.6.7")
}
