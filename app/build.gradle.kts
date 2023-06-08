plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("androidx.navigation.safeargs.kotlin")
  id("kotlinx-serialization")
  id("kotlin-parcelize")
  id("com.google.devtools.ksp") version "1.8.21-1.0.11"
}

kotlin {
  jvmToolchain(11)
}

android {
  namespace = "com.example.movies"
  compileSdk = 33

  defaultConfig {
    applicationId = "com.example.movies"
    minSdk = 23
    targetSdk = 33
    versionCode = 1
    versionName = "1.0.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = true
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
    viewBinding = true
    buildConfig = true
  }
}

ksp {
  arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
  implementation("androidx.datastore:datastore-preferences:1.0.0")

  implementation("com.github.bumptech.glide:glide:4.15.1")

  implementation("com.jakewharton.timber:timber:5.0.1")

  val pagingVersion = "3.1.1"
  implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")

  val koinAndroidVersion = "3.4.1"
  implementation("io.insert-koin:koin-android:$koinAndroidVersion")

  val ktorVersion = "2.3.1"
  implementation("io.ktor:ktor-client-core:$ktorVersion")
  implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
  implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
  implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

  implementation("androidx.room:room-ktx:2.5.1")
  implementation("androidx.room:room-paging:2.5.1")
  ksp("androidx.room:room-compiler:2.5.1")

  implementation("androidx.core:core-ktx:1.10.1")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.9.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
  implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
  implementation("androidx.legacy:legacy-support-v4:1.0.0")
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}