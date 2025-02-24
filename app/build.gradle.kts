import java.io.FileInputStream
import java.util.Properties

plugins {
    // version catalog에 등록된 플러그인을 사용 (catalog에서 버전 관리)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android) // catalog에 kotlin 버전 2.0.21가 반영되어 있어야 합니다.
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
}

val localProps = Properties()
val localFile = rootProject.file("local.properties")
if (localFile.exists()) {
    FileInputStream(localFile).use { localProps.load(it) }
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
        // local.properties의 API 키를 BuildConfig.API_KEY로 설정
        buildConfigField("String", "API_KEY", "\"$openAiKey\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        buildConfig = true
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

    // Guava (ListenableFuture 관련)
    implementation("com.google.guava:guava:31.1-android")

    // 이미지 로딩 라이브러리
    implementation("io.coil-kt:coil-compose:2.3.0")

    // Google ML Kit (필기 인식)
    implementation("com.google.mlkit:digital-ink-recognition:18.1.0")

    // 코루틴 (비동기 처리)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")

    // Lottie 애니메이션
    implementation("com.airbnb.android:lottie-compose:6.6.2")

    // 권한 요청 라이브러리
    implementation(libs.accompanist.permissions)

    // Room Database (로컬 DB)
    implementation("com.airbnb.android:lottie-compose:6.6.2")
    implementation(libs.accompanist.permissions)
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    // CameraX (카메라 기능)
    val cameraxVersion = "1.3.1"
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")

    // 네트워크 요청 (OkHttp & Retrofit)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Fragment & Activity KTX
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    implementation("androidx.activity:activity-ktx:1.10.0")

    // Gson (JSON 파싱용)
    implementation("com.google.code.gson:gson:2.10.1")

    // Compose 관련 라이브러리
    implementation("androidx.compose.runtime:runtime:1.6.7")
    implementation("com.github.commandiron:WheelPickerCompose:1.1.11")

    // AdMob
    implementation("com.google.android.gms:play-services-ads:22.4.0")
}
