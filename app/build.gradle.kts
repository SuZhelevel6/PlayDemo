plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.suzhe.playdemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.suzhe.playdemo"
        minSdk = 33
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ndk {
            // 根据需要添加必要的ABI
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = false
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.0") //AndroidX Core 库
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2") //Android 生命周期相关的 Kotlin 扩展
    implementation("androidx.activity:activity-ktx:1.9.0") //Android Activity 的 Kotlin 扩展
    implementation("androidx.appcompat:appcompat:1.4.1") // AppCompat 库: 对新版本 Android 特性的向后兼容支持
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //日志框架
    //https://github.com/JakeWharton/timber
    implementation ("com.jakewharton.timber:timber:5.0.1")

    //UI框架
    //https://qmuiteam.com/android/get-started
    implementation ("com.qmuiteam:qmui:2.1.0")

    //腾讯开源的高性能keyValue存储，用来替代系统的SharedPreferences
    //https://github.com/Tencent/MMKV
    implementation ("com.tencent:mmkv-static:1.2.16")

    //爱奇艺开源的在android平台上面捕获异常的开源库
    //https://github.com/iqiyi/xCrash
    implementation("com.iqiyi.xcrash:xcrash-android-lib:3.1.0")
}