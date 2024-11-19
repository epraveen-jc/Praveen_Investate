plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.praveen_investate"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.praveen_investate"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}



dependencies {


    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("jp.wasabeef:glide-transformations:4.3.0")
    implementation(libs.recyclerview)
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")


    implementation("org.java-websocket:Java-WebSocket:1.5.2")

    

    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.gms:play-services-maps:18.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1") // For backward compatibility
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // For layout
    implementation("com.google.code.gson:gson:2.10.1") // For JSON parsing
    implementation("androidx.cardview:cardview:1.0.0") // Optional: For card layouts
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1") // For lifecycle-aware components

    // Additional dependencies
    implementation("com.google.android.material:material:1.10.0") // For Material components
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // If using coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // If using coroutines

    // Libraries using aliases
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.storage) // Ensure this points to the same version
    implementation(libs.play.services.location)

    // Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
