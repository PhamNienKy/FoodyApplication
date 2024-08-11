
plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "foody.vn"
    compileSdk = 34

    defaultConfig {
        applicationId = "foody.vn"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "5.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}


dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //noinspection BomWithoutPlatform,UseTomlInstead
    implementation("com.google.firebase:firebase-bom:33.1.0")
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-analytics:22.0.1")
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-database:21.0.0")
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-auth:23.0.0")
    //noinspection GradleDependency,UseTomlInstead
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    //noinspection UseTomlInstead
    implementation("com.facebook.android:facebook-login:17.0.0")
    //noinspection UseTomlInstead
    implementation("de.hdodenhof:circleimageview:3.1.0")
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-storage:21.0.0")
    //noinspection UseTomlInstead
    implementation("com.google.android.gms:play-services-location:21.3.0")
    //noinspection UseTomlInstead,GradleDependency
    implementation("androidx.appcompat:appcompat:1.2.0")
    //noinspection GradleDependency,UseTomlInstead
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //noinspection UseTomlInstead
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    //noinspection UseTomlInstead
    implementation("com.google.maps.android:android-maps-utils:2.2.5")
}