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

        // 开启DialogX组件的实时模糊效果
        renderscriptTargetApi = 21
        renderscriptSupportModeEnabled = true
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("/Users/zhangtao/Documents/Code/keystore/x5platform.keystore")
            storePassword = "giec_stb_666"
            keyAlias = "giec"
            keyPassword = "giec_stb_666"
        }
    }
    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
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
        compose = false
        viewBinding = true
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
    implementation("androidx.databinding:viewbinding:8.9.0")
    implementation("androidx.activity:activity:1.8.0")
    implementation("androidx.compose.ui:ui-text-android:1.7.8")
    implementation("com.google.ai.edge.litert:litert:2.0.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //UI框架
    //https://qmuiteam.com/android/get-started
    implementation("com.qmuiteam:qmui:2.1.0")

    //腾讯开源的高性能keyValue存储，用来替代系统的SharedPreferences
    //https://github.com/Tencent/MMKV
    implementation("com.tencent:mmkv-static:1.2.16")

    //爱奇艺开源的在android平台上面捕获异常的开源库
    //https://github.com/iqiyi/xCrash
    implementation("com.iqiyi.xcrash:xcrash-android-lib:3.1.0")

    //apache common lang3工具包
    //提供了StringUtils等这样的类
    //http://commons.apache.org/proper/commons-lang/
    implementation("org.apache.commons:commons-lang3:3.8")

    //android常用工具类
    //https://github.com/Blankj/AndroidUtilCode
    //https://github.com/Blankj/AndroidUtilCode/blob/master/lib/utilcode/README-CN.md
    implementation("com.blankj:utilcodex:1.31.1")

    // Dialog工具DialogX
    // https://github.com/kongzue/DialogX
    val dialogx_version = "0.0.50.beta33"
    implementation("com.github.kongzue.DialogX:DialogX:${dialogx_version}")
    implementation("com.github.kongzue.DialogX:DialogXIOSStyle:${dialogx_version}")
    implementation("com.github.kongzue.DialogX:DialogXKongzueStyle:${dialogx_version}")
    implementation("com.github.kongzue.DialogX:DialogXMIUIStyle:${dialogx_version}")
    implementation("com.github.kongzue.DialogX:DialogXMaterialYou:${dialogx_version}")
    implementation("com.kongzue.stacklabel:StackLabel:1.2.0")

    //类似TabLayout的控件
    //https://github.com/angcyo/DslTabLayout
    implementation("com.github.angcyo.DslTablayout:TabLayout:3.5.3")
    implementation("com.github.angcyo.DslTablayout:ViewPager2Delegate:3.5.3")

    //DSLAdapter
    implementation("com.github.angcyo:DslAdapter:6.0.4")

    implementation("io.github.cymchad:BaseRecyclerViewAdapterHelper4:4.1.4")

    // 自定义的Android下拉选择器组件
    // https://blog.csdn.net/web_longboss/article/details/80820608
    // https://github.com/jaredrummler/MaterialSpinner
    implementation("com.jaredrummler:material-spinner:1.3.1")

    //圆形指示器
    //https://github.com/ongakuer/CircleIndicator
    implementation("me.relex:circleindicator:2.1.6")

    //webview进度条
    //https://github.com/youlookwhat/WebProgress
    implementation("com.github.youlookwhat:WebProgress:1.2.0")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")

    implementation("com.tencent.bugly:crashreport:4.1.9.3")

    implementation(files("libs/ShellCmd222.jar"))

    implementation("com.qmdeve.liquidglass:core:1.0.0")
}