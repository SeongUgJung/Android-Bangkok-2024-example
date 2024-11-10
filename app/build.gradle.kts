plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.secret)
    alias(libs.plugins.kotlin.serialize)
}

android {
    namespace = "com.android.bangkok2024.example"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.android.bangkok2024.example"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("$rootDir/config/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }
}

tasks.withType<Test>() {
    useJUnitPlatform()
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "secrets.local.properties"
}

dependencies {
    implementation(libs.kotlin.serialize.json)

    implementation(platform(libs.firebase.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.firebase.database.ktx)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.runtime)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.animation)

    implementation(libs.kotlin.coroutine.core)
    implementation(libs.kotlin.coroutine.android)

    testImplementation(libs.kotlin.coroutine.test)
    testImplementation(platform(libs.test.junit5.bom))
    testImplementation(libs.test.junit5.jupiter.api)
    testRuntimeOnly(libs.test.junit5.jupiter.engine)
    testRuntimeOnly(libs.test.junit5.vintage.engine)

}

apply(plugin = libs.plugins.google.playservice.get().pluginId)