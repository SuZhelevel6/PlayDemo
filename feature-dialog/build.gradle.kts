plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.suzhe.feature.dialog"
    compileSdk = 34

    defaultConfig {
        minSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":lib-base"))
    implementation(project(":lib-common"))

    // DialogX
    val dialogx_version = "0.0.50.beta33"
    api("com.github.kongzue.DialogX:DialogX:${dialogx_version}")
    api("com.github.kongzue.DialogX:DialogXIOSStyle:${dialogx_version}")
    api("com.github.kongzue.DialogX:DialogXKongzueStyle:${dialogx_version}")
    api("com.github.kongzue.DialogX:DialogXMIUIStyle:${dialogx_version}")
    api("com.github.kongzue.DialogX:DialogXMaterialYou:${dialogx_version}")

    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.9.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
}
