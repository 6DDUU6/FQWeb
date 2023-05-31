plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val keyProps = `java.util`.Properties()
val keyPropsFile: File = rootProject.file("keystore/keystore.properties")
if (keyPropsFile.exists()) {
    keyProps.load(`java.io`.FileInputStream(keyPropsFile))
}

android {
    namespace = "me.fycz.fqweb"
    compileSdk = 33

    defaultConfig {
        applicationId = "me.fycz.fqweb"
        minSdk = 24
        targetSdk = 33
        versionCode = 10
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        register("myConfig") {
            keyAlias = keyProps["keyAlias"].toString()
            keyPassword = keyProps["keyPassword"].toString()
            storeFile = file(keyProps["storeFile"].toString())
            storePassword = keyProps["storePassword"].toString()
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (keyPropsFile.exists()) {
                signingConfig = signingConfigs.getByName("myConfig")
            }
        }
    }
    android.applicationVariants.all {
        val fileName = "FQWeb_v${defaultConfig.versionName}.apk"
        outputs.map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                output.outputFileName = fileName
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
    implementation("org.nanohttpd:nanohttpd:2.3.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}