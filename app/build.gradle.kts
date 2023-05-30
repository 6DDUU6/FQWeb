plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "me.fycz.fqweb"
    compileSdk =  33

    defaultConfig {
        applicationId = "me.fycz.fqweb"
        minSdk = 24
        targetSdk = 33
        versionCode = 10
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    compileOnly(files("libs/api-82.jar"))

    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    //webServer
    val nanoHttpdVersion = "2.3.1"
    implementation("org.nanohttpd:nanohttpd:$nanoHttpdVersion")
    implementation("org.nanohttpd:nanohttpd-websocket:$nanoHttpdVersion")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}