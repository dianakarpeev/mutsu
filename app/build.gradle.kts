import com.google.protobuf.gradle.id

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.protobuf") version "0.9.4"
    id("com.google.gms.google-services")
    id("com.diffplug.spotless") version "6.23.3"
}

android {
    namespace = "com.example.mutsu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mutsu"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
            isDebuggable = true
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    spotless {
        kotlin {
            ktfmt()
            ktlint()
            diktat()
            prettier()
        }
        kotlinGradle {
            ktlint()
            diktat()
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-runtime-ktx:2.6.0")
    implementation("androidx.benchmark:benchmark-macro:1.2.1")
    implementation("androidx.compose.ui:ui-text-android:1.5.4")
    testImplementation("junit:junit:4.13.2")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1")

    // stateflow
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // navigation
    implementation("androidx.navigation:navigation-compose:2.6.0")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    // implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("com.google.firebase:firebase-auth-ktx")

    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Proto DataStore
    implementation("androidx.datastore:datastore-core:1.0.0")
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("com.google.protobuf:protobuf-lite:3.0.1")
    compileOnly("javax.annotation:javax.annotation-api:1.2")
}
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.19.4"
    }
    plugins {
        id("lite") {
            artifact = "com.google.protobuf:protoc-gen-javalite:3.0.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                id("lite")
            }
        }
    }
}
